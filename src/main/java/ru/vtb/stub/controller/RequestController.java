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
    public ResponseEntity<StubData> getDataByKey(@RequestParam @RouteKey String key) {
        return ResponseEntity.ok(service.getDataByKey(key));
    }

    @GetMapping("/{team}")
    public ResponseEntity<StubData[]> getTeamData(@PathVariable String team) {
        return ResponseEntity.ok(service.getTeamData(team));
    }

    @DeleteMapping
    public ResponseEntity<StubData> removeData(@RequestParam @RouteKey String key) {
        var data = service.removeDataByKey(key);
        return data != null ? ResponseEntity.ok(data) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{team}")
    public ResponseEntity<StubData[]> removeTeamData(@PathVariable String team) {
        var data = service.removeTeamData(team);
        return data != null ? ResponseEntity.ok(data) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
