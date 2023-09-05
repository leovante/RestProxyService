package ru.vtb.stub.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ResponseController {

    @Autowired
    private ResponseService service;

    @Operation(hidden = true)
    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE},
            produces = {APPLICATION_JSON_VALUE,  TEXT_PLAIN_VALUE, "application/*"})
    public ResponseEntity<Object> response(
            @RequestParam String rpsRequest,
            @RequestParam String rpsKey,
            RequestWrapper servletRequest
    ) {
        return service.sendResponse(rpsRequest, rpsKey, servletRequest);
    }
}
