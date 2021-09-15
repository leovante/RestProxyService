package ru.vtb.stub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.validate.RouteKey;

import javax.validation.Valid;

@RestController
@RequestMapping("${path.admin}")
public class RequestController {

    @Autowired
    private RequestService service;

    @PostMapping
    public ResponseEntity<Void> putData(@Valid @RequestBody StubData data) {
        service.putData(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<StubData> getData(@RequestParam @RouteKey String key) {
        return ResponseEntity.ok(service.getData(key));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeData(@RequestParam @RouteKey String key) {
        service.removeAllData(key);
        return ResponseEntity.ok().build();
    }
}
