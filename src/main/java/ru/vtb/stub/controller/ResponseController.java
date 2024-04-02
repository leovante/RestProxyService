package ru.vtb.stub.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.stub.filter.RequestWrapper;
import ru.vtb.stub.service.ResponseService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService service;

    @Operation(hidden = true)
    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE},
            produces = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE})
    public ResponseEntity<Object> response(
            @RequestParam String rpsRequest,
            @RequestParam String rpsKey,
            RequestWrapper servletRequest
    ) {
        var resp = service.sendResponse(rpsRequest, rpsKey, servletRequest);
        return ResponseEntity.ok(resp);
    }
}
