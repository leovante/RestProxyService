package ru.vtb.stub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static ru.vtb.stub.data.ResponseData.responseData;

@Slf4j
@RestController
public class ResponseDataController {

    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE})
    public ResponseEntity<Object> response(@RequestParam String key) {
        var request = key.split(":");

        HttpStatus status = (responseData.get(key).get("status") != null)
                ? HttpStatus.valueOf((Integer) responseData.get(key).get("status"))
                : setResponseStatus(request[1]);

        StringBuilder info = new StringBuilder("Response data controller. Send response. Status: " + status);
        var response = ResponseEntity.status(status);

        if (responseData.get(key).get("headers") != null) {
            info.append(". Headers: ");
            Map<String, String> headersMap = (Map<String, String>) responseData.get(key).get("headers");
            headersMap.forEach((k, v) -> {
                info.append(k).append(" --> ").append(v).append(", ");
                response.header(k, v);
            });
            info.setLength(info.length() - 2);
        }
        if (responseData.get(key).get("body") != null) {
            var body = responseData.get(key).get("body");
            info.append(". Body: ").append(body);
            log.info(info.toString());
            return response.body(body);
        }
        log.info(info.toString());
        return ResponseEntity.status(status).build();
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
