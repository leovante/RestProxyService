package ru.vtb.stub.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.filter.RequestWrapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.DataMap.*;

@Slf4j
@Service
public class ResponseService {

    @SneakyThrows
    public ResponseEntity<Object> sendResponse(String key, String regexKey, RequestWrapper servletRequest) {
        var dataKey = dataByKeyMap.containsKey(key) ? key : regexKey;
        StubData data = dataKey.endsWith("$") ? dataByRegexMap.get(regexKey) : dataByKeyMap.get(key);
        var history = requestMap.computeIfAbsent(dataKey, k -> new ArrayList<>());
        var wait = data.getWait();
        var responseData = data.getResponse();
        var status = HttpStatus.valueOf(responseData.getStatus());
        var request = Request.builder()
                .date(LocalDateTime.now())
                .path(key.split(":")[0])
                .method(key.split(":")[1])
                .headers(getHeaders(servletRequest))
                .params(getQueryParams(servletRequest.getQueryString()))
                .body(servletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())))
                .build();

        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", dataKey, wait);
            Thread.sleep(wait);
        }
        if (status.value() >= 400) {
            log.info("Request to: {} --> Response with error: {}", dataKey, status);
            updateHistory(history, request, dataKey);
            throw new ResponseStatusException(status, "Test error message");
        }
        var response = ResponseEntity.status(status);
        if (!ObjectUtils.isEmpty(responseData.getHeaders()))
            responseData.getHeaders().forEach(h -> response.header(h.getName(), h.getValue()));
        log.info("Request to: {} --> {}", dataKey, responseData);
        updateHistory(history, request, dataKey);
        return responseData.getBody() != null ? response.body(responseData.getBody()) : response.build();
    }

    private Map<String, String> getHeaders(RequestWrapper request) {
        var headers = request.getHeaderNames();
        if (ObjectUtils.isEmpty(headers)) return null;
        return Collections.list(headers).stream().collect(Collectors.toMap(h -> h, request::getHeader));
    }

    private Map<String, String> getQueryParams(String queryString) {
        String[] params = queryString.split("&");
        if (params.length == 0) return null;
        return Arrays.stream(params).map(p -> p.split("=")).skip(2).collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }

    private void updateHistory(List<Request> history, Request request, String key) {
        history.add(request);
        log.debug("Updated history: {} --> {}", key, requestMap.get(key));
    }
}
