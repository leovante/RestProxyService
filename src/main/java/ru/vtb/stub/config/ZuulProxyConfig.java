package ru.vtb.stub.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vtb.stub.filter.HostFilter;

@Configuration
public class ZuulProxyConfig {

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
}
