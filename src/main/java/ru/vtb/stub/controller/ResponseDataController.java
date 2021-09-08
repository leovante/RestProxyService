package ru.vtb.stub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static ru.vtb.stub.data.ResponseData.responseData;

@Slf4j
@RestController
public class ResponseDataController {

    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE})
    public ResponseEntity<Object> response(@RequestParam String key) {
        var request = key.split(":");
        log.info("Response data controller. Incoming request to: {}", key);

        HttpStatus status = (responseData.get(key).get("status") != null)
                ? HttpStatus.valueOf((Integer) responseData.get(key).get("status"))
                : setResponseStatus(request[1]);

        var body = responseData.get(key).get("body");
        log.info("Response data controller. Send response. Status: {}, body: {}", status, body);

        return ResponseEntity.status(status).body(body);
    }

    private HttpStatus setResponseStatus(String method) {
        switch (method) {
            case "POST":
                return HttpStatus.valueOf(201);
            case "PUT":
            case "PATCH":
                return HttpStatus.valueOf(204);
            default:
                return HttpStatus.valueOf(200);
        }
    }
}
