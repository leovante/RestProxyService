package ru.vtb.stub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.validate.Method;
import ru.vtb.stub.validate.Path;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("${path.admin}")
public class RequestController {

    @Autowired
    private RequestService service;

    @PostMapping
    @Operation(summary = "Установка данных для endpoint и method")
    public ResponseEntity<Void> putData(@Valid @RequestBody StubData data) {
        service.putData(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Получение данных, установленных для endpoint и method")
    @Parameter(name = "path", description = "Пример: /path/example")
    @Parameter(name = "method", description = "Пример: GET")
    public ResponseEntity<StubData> getDataByKey(@RequestParam @Path String path, @RequestParam @Method String method) {
        return ResponseEntity.ok(service.getDataByKey(path + ":" + method));
    }

    @GetMapping("/{team}")
    public ResponseEntity<StubData[]> getTeamData(@PathVariable String team) {
        return ResponseEntity.ok(service.getTeamData(team));
    }

    @DeleteMapping
    public ResponseEntity<StubData> removeData(@RequestParam @Path String path, @RequestParam @Method String method) {
        var data = service.removeDataByKey(path + ":" + method);
        return data != null ? ResponseEntity.ok(data) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{team}")
    public ResponseEntity<StubData[]> removeTeamData(@PathVariable String team) {
        return service.removeTeamData(team) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<Request>> getHistoryByKey(@RequestParam @Path String path, @RequestParam @Method String method) {
        return ResponseEntity.ok(service.getHistoryByKey(path + ":" + method));
    }
}
