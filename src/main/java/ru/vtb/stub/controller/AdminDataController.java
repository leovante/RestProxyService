package ru.vtb.stub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vtb.stub.service.AdminDataService;
import ru.vtb.stub.validate.ResponseKey;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;

@Slf4j
@Validated
@RestController
@RequestMapping("${path.admin.data}")
public class AdminDataController {

    @Autowired
    private AdminDataService service;

    @GetMapping
    public ResponseEntity<Object> getResponseData(@RequestParam @ResponseKey String key) {
        log.info("Admin data controller. Get response data for key: {}", key);
        return ResponseEntity.ok(service.getResponseData(key));
    }

    @PostMapping
    public ResponseEntity<Void> putResponseData(
            @RequestParam @ResponseKey String key,
            @RequestParam(required = false) @Digits(integer = 3, fraction = 0) @Max(value = 399) Integer status,
            @RequestBody(required = false) Object body
    ) {
        log.info("Admin data controller. Put response data for key: {}", key);
        service.putResponseData(key, body, status);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeResponseData(@RequestParam @ResponseKey String key) {
        log.info("Admin data controller. Delete response data for key: {}", key);
        return service.removeResponseData(key) != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
