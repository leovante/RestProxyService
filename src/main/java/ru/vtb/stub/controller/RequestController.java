package ru.vtb.stub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.validate.RouteKey;

@Slf4j
@RestController
@RequestMapping("${path.admin}")
public class RequestController {

    @Autowired
    private RequestService service;

    @PostMapping
    public ResponseEntity<Void> putResponseData(@RequestBody StubData data) {
        log.debug("Admin data controller. Put data: {}", data);
        service.putData(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<StubData> getResponseData(@RequestParam @RouteKey String key) {
        log.debug("Admin data controller. Get data for key: {}", key);
        return ResponseEntity.ok(service.getData(key));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeResponseData(@RequestParam @RouteKey String key) {
        log.debug("Admin data controller. Delete all data for key: {}", key);
//        return service.removeAllData(key) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        service.removeAllData(key);
        return ResponseEntity.ok().build();
    }
}
