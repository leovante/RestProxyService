package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

@Slf4j
@Service
public class ZuulProxyService {

    @Autowired
    private ZuulProperties zuulProperties;

    @Autowired
    public RoutesRefreshedEvent routesRefreshedEvent;

    @Autowired
    public ApplicationEventPublisher publisher;

    public ZuulRoute getProxyHost(String id) {
        var route = zuulProperties.getRoutes().get(id);
        if (route != null)
            log.info("Zuul proxy service. Get route: {}", route);
        else
            log.info("Zuul proxy service. No route for id: {}", id);
        return route;
    }

    public void putProxyHost(String id, String url, String path) {
        ZuulRoute route = new ZuulRoute();
        route.setId(id);
        route.setUrl(url);
        route.setPath(path);

        zuulProperties.getRoutes().put(route.getId(), route);
        publisher.publishEvent(routesRefreshedEvent);
        log.info("Zuul proxy service. Published new route: {}", route);
    }

    public ZuulRoute removeProxyHost(String id) {
        var route = zuulProperties.getRoutes().remove(id);
        publisher.publishEvent(routesRefreshedEvent);
        if (route != null)
            log.info("Zuul proxy service. Delete route: {}", route);
        else
            log.info("Zuul proxy service. No route for id: {}", id);
        return route;
    }
}
