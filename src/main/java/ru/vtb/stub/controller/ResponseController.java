package ru.vtb.stub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static ru.vtb.stub.data.DataMap.dataMap;

@Slf4j
@RestController
public class ResponseController {

    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE})
    public ResponseEntity<Object> response(@RequestParam String key) {

        var data = dataMap.get(key).getResponse();
        log.info("Request to: {} --> {}", key, data);

        var response = ResponseEntity.status(data.getStatus());

        if (data.getHeaders() != null && !data.getHeaders().isEmpty()) {
            data.getHeaders().forEach(h -> response.header(h.getName(), h.getValue()));
        }
        return data.getBody() != null ? response.body(data.getBody()) : response.build();
    }
}
