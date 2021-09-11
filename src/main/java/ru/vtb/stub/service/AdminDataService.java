package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.ResponseData.responseData;

@Slf4j
@Service
public class AdminDataService {

    @Value("${prefix.header}")
    private String headerPrefix;

    public Object getResponseData(String key) {
        var data = responseData.get(key);
        if (data != null)
            log.debug("Admin data service. Get response data: {}", data);
        else
            log.debug("Admin data service. No response data for key: {}", key);
        return data;
    }

    public void putResponseData(String key, Integer status, Map<String, String> headers, Object body) {
        if (!responseData.containsKey(key)) responseData.put(key, new HashMap<>());
        var data = responseData.get(key);

        var responseHeaders = headers.keySet().stream()
                .filter(k -> k.startsWith(headerPrefix))
                .collect(Collectors.toMap(k -> k.split(headerPrefix, 2)[1], headers::get));

        if (status == null && responseHeaders.isEmpty() && body == null)
            log.debug("Admin data service. Response status, headers and body are not passed");

        if (status != null) {
            log.debug("Admin data service. Set response status: {}", status);
            data.put("status", status);
        }
        if (!responseHeaders.isEmpty()) {
            responseHeaders.forEach((k, v) -> log.debug("Admin data service. Set response header: {} --> {}", k, v));
            data.put("headers", responseHeaders);
        }
        if (body != null) {
            log.debug("Admin data service. Set response body: {}", body);
            data.put("body", body);
        }
    }

    public Object removeResponseData(String key) {
        var data = responseData.remove(key);
        if (data != null)
            log.debug("Admin data service. Delete response data: {}", data);
        else
            log.debug("Admin data service. No response data for key: {}", key);
        return data;
    }
}
