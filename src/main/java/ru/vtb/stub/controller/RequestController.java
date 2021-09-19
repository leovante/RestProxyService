package ru.vtb.stub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Установка данных для end-point и method")
    @ApiResponse(responseCode = "201", description = "Данные установлены")
    @ApiResponse(responseCode = "400", description = "Ошибка в формате данных")
    public ResponseEntity<Void> putData(@Valid @RequestBody StubData data) {
        service.putData(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Получение данных, установленных для end-point и method")
    @Parameter(name = "path", description = "пример: /path/example")
    @Parameter(name = "method", description = "пример: GET")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = @Content(schema = @Schema(implementation = StubData.class)))
    public ResponseEntity<StubData> getData(@RequestParam @Path String path, @RequestParam @Method String method) {
        return ResponseEntity.ok(service.getData(path + ":" + method));
    }

    @GetMapping("/{team}")
    @Operation(summary = "Получение всех данных, установленных для префикса (команды)")
    @Parameter(name = "team", description = "пример: team1")
    @ApiResponse(responseCode = "200", description = "Данные получены", content = @Content(array = @ArraySchema(schema = @Schema(implementation = StubData.class))))
    public ResponseEntity<StubData[]> getTeamData(@PathVariable String team) {
        return ResponseEntity.ok(service.getTeamData(team));
    }

    @DeleteMapping
    @Operation(summary = "Удаление данных, установленных для end-point и method")
    @Parameter(name = "path", description = "пример: /path/example")
    @Parameter(name = "method", description = "пример: GET")
    @ApiResponse(responseCode = "200", description = "Данные удалены", content = @Content(schema = @Schema(implementation = StubData.class)))
    @ApiResponse(responseCode = "204", description = "Нет данных")
    public ResponseEntity<StubData> removeData(@RequestParam @Path String path, @RequestParam @Method String method) {
        var data = service.removeData(path + ":" + method);
        return data != null ? ResponseEntity.ok(data) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{team}")
    @Operation(summary = "Удаление всех данных, установленных для префикса (команды)")
    @Parameter(name = "team", description = "пример: team1")
    @ApiResponse(responseCode = "200", description = "Данные удалены")
    public ResponseEntity<StubData[]> removeTeamData(@PathVariable String team) {
        return service.removeTeamData(team) ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/history")
    @Operation(summary = "Получение истории запросов для end-point и method")
    @Parameter(name = "path", description = "пример: /path/example")
    @Parameter(name = "method", description = "пример: GET")
    @ApiResponse(responseCode = "200", description = "История запросов получена", content = @Content(schema = @Schema(implementation = Request.class)))
    public ResponseEntity<List<Request>> getHistory(@RequestParam @Path String path, @RequestParam @Method String method) {
        return ResponseEntity.ok(service.getHistory(path + ":" + method));
    }
}
