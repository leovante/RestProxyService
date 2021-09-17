package ru.vtb.stub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.stub.filter.RequestWrapper;
import ru.vtb.stub.service.ResponseService;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ResponseController {

    @Autowired
    private ResponseService service;

    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE})
    public ResponseEntity<Object> response(@RequestParam String key, RequestWrapper servletRequest) {

        return service.sendResponse(key, servletRequest);
    }
}
