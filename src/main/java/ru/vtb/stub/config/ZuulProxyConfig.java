package ru.vtb.stub.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vtb.stub.filter.HostFilter;

import java.util.HashMap;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "prefix")
public class ZuulProxyConfig {

    private List<String> teams;

    @Bean
    public HostFilter simpleFilter() {
        return new HostFilter();
    }

    @Bean("discoveryClientRouteLocator")
    public DiscoveryClientRouteLocator discoveryClientRouteLocator(
            ServerProperties server,
            ZuulProperties zuulProperties,
            DiscoveryClient discovery,
            ServiceRouteMapper serviceRouteMapper,
            @Autowired(required = false) Registration registration) {
        return new DiscoveryClientRouteLocator(
                server.getServlet().getContextPath(), discovery, zuulProperties, serviceRouteMapper, registration
        );
    }

    @Bean
    public RoutesRefreshedEvent routesRefreshedEvent(@Qualifier("discoveryClientRouteLocator") DiscoveryClientRouteLocator locator) {
        return new RoutesRefreshedEvent(locator);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean(
            RoutesRefreshedEvent routesRefreshedEvent,
            ZuulProperties zuulProperties,
            ApplicationEventPublisher publisher
    ) {
        return (args) -> {
            if (zuulProperties.getRoutes().isEmpty()) {
                log.info("Zuul proxy config. No default routes");
                return;
            }

            if (teams == null || teams.isEmpty()) {
                log.info("Zuul proxy config. No teams prefixes. Default routes:");
                zuulProperties.getRoutes().forEach((k, v) -> log.info("{} --> {}", k, v));
                return;
            }

            var routes = new HashMap<>(zuulProperties.getRoutes());
            teams.forEach(p -> routes.forEach((k, r) -> {
                ZuulRoute route = new ZuulRoute();
                route.setId(p + "-" + k);
                route.setPath("/" + p + r.getPath());
                route.setUrl(r.getUrl());
                zuulProperties.getRoutes().put(route.getId(), route);
            }));

            routes.keySet().forEach(k -> zuulProperties.getRoutes().remove(k)); // delete default routes

            publisher.publishEvent(routesRefreshedEvent);
            log.info("Zuul proxy config. Teams routes:");
            zuulProperties.getRoutes().forEach((k, v) -> log.info("{} --> {}", k, v));
        };
    }
}
