package ru.vtb.stub.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.filter.RequestWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.DataMap.*;

@Slf4j
@Service
public class ResponseService {

    @SneakyThrows
    public ResponseEntity<Object> sendResponse(String rpsRequest, String key, RequestWrapper servletRequest) {

        StubData data = key.endsWith("$") ? dataByRegexMap.get(key) : dataByKeyMap.get(key);

        var request = Request.builder()
                .date(LocalDateTime.now())
                .path(rpsRequest.split(":")[0])
                .method(rpsRequest.split(":")[1])
                .headers(getHeaders(servletRequest))
                .params(getQueryParams(servletRequest.getQueryString()))
                .body(servletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())))
                .build();

        var wait = data.getWait();
        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", key, wait);
            Thread.sleep(wait);
        }

        var responseList = data.getResponses();
        var responseData = data.getResponse();
        Response actualData;

        // Если заполнены поля responses и response, то приоритет у responses
        if (!ObjectUtils.isEmpty(responseList)) {
            int count = data.getCount();
            // При повторном запросе будет отдан следующий элемент responseList
            int next = count + 1;
            data.setCount((next == responseList.size()) ? 0 : next);
            actualData = responseList.get(count);
        } else {
            actualData = responseData;
        }

        var jsonBody = actualData.getBody();
        var stringBody = actualData.getStringBody();
        Object actualBody;

        // Если заполнены поля body (json) и stringBody, приоритет у body
        if (jsonBody != null) {
            actualBody = jsonBody;
        } else if (stringBody != null) {
            actualBody = stringBody.getBytes(StandardCharsets.UTF_8);
        } else {
            actualBody = null;
        }

        var status = HttpStatus.valueOf(actualData.getStatus());
        var history = requestMap.computeIfAbsent(key, k -> new ArrayList<>());

        if (status.value() >= 400) {
            updateHistory(history, request, key);
            if (actualBody != null) {
                log.info("Request to: {} --> Response with error: {}, body: {}", key, status, actualBody);
                return ResponseEntity.status(status)
                        .body(actualBody);
            } else {
                log.info("Request to: {} --> Response with error: {}", key, status);
            }
            throw new ResponseStatusException(status, "Test error message");
        }

        var headers = actualData.getHeaders();
        var response = ResponseEntity.status(status);

        if (!ObjectUtils.isEmpty(headers)) {
            headers.forEach(h -> response.header(h.getName(), h.getValue()));
        }

        log.info("Request to: {} --> {}", key, actualData);
        updateHistory(history, request, key);
        return actualBody != null ? response.body(actualBody) : response.build();
    }

    private Map<String, String> getHeaders(RequestWrapper request) {
        var headers = request.getHeaderNames();
        if (ObjectUtils.isEmpty(headers)) return null;
        return Collections.list(headers).stream().collect(Collectors.toMap(h -> h, request::getHeader));
    }

    private Map<String, String> getQueryParams(String queryString) {
        String[] params = queryString.split("&");
        if (params.length == 0) return null;
        return Arrays.stream(params).map(p -> p.split("=")).skip(2).collect(Collectors.toMap(p -> p[0], p -> p.length > 1 ? p[1] : ""));
    }

    private void updateHistory(List<Request> history, Request request, String key) {
        history.add(request);
        log.debug("Updated history: {} --> {}", key, requestMap.get(key));
    }
}
