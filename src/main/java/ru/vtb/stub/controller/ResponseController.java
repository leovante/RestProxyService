package ru.vtb.stub.controller;

import lombok.SneakyThrows;
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

    @SneakyThrows
    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE})
    public ResponseEntity<Object> response(@RequestParam String key) {

        var data = dataMap.get(key);
        var wait = data.getWait();
        if (wait != null) {
            log.info("Request to: {} --> Waiting in {} ms...", key, wait);
            Thread.sleep(wait);
        }

        var responseData = data.getResponse();
        log.info("Request to: {} --> {}", key, responseData);
        var response = ResponseEntity.status(responseData.getStatus());
        if (responseData.getHeaders() != null && !responseData.getHeaders().isEmpty()) {
            responseData.getHeaders().forEach(h -> response.header(h.getName(), h.getValue()));
        }
        return responseData.getBody() != null ? response.body(responseData.getBody()) : response.build();
    }
}
