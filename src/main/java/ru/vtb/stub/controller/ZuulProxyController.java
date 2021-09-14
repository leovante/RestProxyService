package ru.vtb.stub.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vtb.stub.service.ZuulProxyService;
import ru.vtb.stub.validate.ProxyId;

import static org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

@Slf4j
@Validated
@RestController
@RequestMapping("${path.proxy}")
public class ZuulProxyController {

    @Autowired
    private ZuulProxyService service;

    @GetMapping
    public ResponseEntity<ZuulRoute> getProxyHost(@RequestParam @ProxyId String id) {
        log.debug("Zuul proxy controller. Get route for id: {}", id);
        return ResponseEntity.ok(service.getProxyHost(id));
    }

    @PostMapping
    public ResponseEntity<Void> putProxyHost(
            @RequestParam @ProxyId String id,
            @RequestParam @URL String url,
            @RequestParam String path
    ) {
        log.debug("Zuul proxy controller. Put route for id: {}", id);
        service.putProxyHost(id, url, path);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeProxyHost(String id) {
        log.debug("Zuul proxy controller. Delete route for id: {}", id);
        return service.removeProxyHost(id) != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
