package ru.vtb.stub.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.filter.RequestWrapper;

import java.util.*;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.DataMap.dataMap;
import static ru.vtb.stub.data.DataMap.requestMap;

@Slf4j
@Service
public class ResponseService {

    @SneakyThrows
    public ResponseEntity<Object> sendResponse(String key, RequestWrapper servletRequest) {
        var data = dataMap.get(key);
        var history = requestMap.computeIfAbsent(key, k -> new ArrayList<>());
        var wait = data.getWait();
        var responseData = data.getResponse();
        var responseError = data.getError();
        var request = Request.builder()
                .path(key.split(":")[0])
                .method(key.split(":")[1])
                .headers(getHeaders(servletRequest))
                .params(getQueryParams(servletRequest.getQueryString()))
                .body(servletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())))
                .build();

        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", key, wait);
            Thread.sleep(wait);
            log.info("Wait is over, response --> {}", responseData);
        }
        if (responseError != null) {
            var status = HttpStatus.valueOf(responseError.getStatus());
            log.info("Request to: {} --> Response with error: {}", key, status);
            updateHistory(history, request, key);
            throw new ResponseStatusException(status);
        }
        if (responseData != null) {
            var response = ResponseEntity.status(responseData.getStatus());
            if (!ObjectUtils.isEmpty(responseData.getHeaders()))
                responseData.getHeaders().forEach(h -> response.header(h.getName(), h.getValue()));
            updateHistory(history, request, key);
            return responseData.getBody() != null ? response.body(responseData.getBody()) : response.build();
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No \"response\" or \"error\" set!");
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

    private void updateHistory(List<Request> history, Request request, String key) {
        history.add(request);
        log.debug("Updated history: {} --> {}", key, requestMap.get(key));
    }
}
