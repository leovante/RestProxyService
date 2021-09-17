package ru.vtb.stub.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.filter.RequestWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.DataMap.dataMap;
import static ru.vtb.stub.data.DataMap.requestMap;

@Slf4j
@Service
public class ResponseService {

    @SneakyThrows
    public ResponseEntity<Object> sendResponse(String key, RequestWrapper servletRequest) {
        var data = dataMap.get(key);
        var wait = data.getWait();
        var responseData = data.getResponse();

        var request = Request.builder()
                .path(key.split(":")[0])
                .method(key.split(":")[1])
                .headers(getHeaders(servletRequest))
                .params(getQueryParams(servletRequest.getQueryString()))
                .body(servletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())))
                .build();

        var requests = requestMap.computeIfAbsent(key, k -> new ArrayList<>());
        requests.add(request);
        log.debug("Updated history: {} --> {}", key, requestMap.get(key));

        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", key, wait);
            Thread.sleep(wait);
            log.info("Wait is over, response --> {}", responseData);
        } else
            log.info("Request to: {} --> {}", key, responseData);

        var response = ResponseEntity.status(responseData.getStatus());
        if (responseData.getHeaders() != null && !responseData.getHeaders().isEmpty()) {
            responseData.getHeaders().forEach(h -> response.header(h.getName(), h.getValue()));
        }
        return responseData.getBody() != null ? response.body(responseData.getBody()) : response.build();
    }

    private Map<String, String> getHeaders(RequestWrapper request) {
        var headers = request.getHeaderNames();
        return Collections.list(headers).stream().collect(Collectors.toMap(h -> h, request::getHeader));
    }

    private Map<String, String> getQueryParams(String queryString) {
        String[] params = queryString.split("&");
        if (params.length == 0) return null;
        return Arrays.stream(params).map(p -> p.split("=")).skip(1).collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }
}
