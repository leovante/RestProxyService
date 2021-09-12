package ru.vtb.stub.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.ResponseData.responseData;
import static ru.vtb.stub.utils.CommonUtils.*;

@Slf4j
@Setter
@Service
@ConfigurationProperties(prefix = "prefix")
public class AdminDataService {

    private String header;
    private Set<String> teams;
    
    private static final String SERVICE = "Admin data service";

    public Object getResponseData(String key) {
        var data = responseData.get(key);
        if (data != null)
            log.debug("{}. Get response data: {}", SERVICE, data);
        else
            log.debug("{}. No response data for: {}", SERVICE, key);
        return data;
    }

    public void putResponseData(String key, Integer status, Map<String, String> headers, Object body) {
        if (!responseData.containsKey(key)) responseData.put(key, new HashMap<>());
        var data = responseData.get(key);

        var responseHeaders = headers.keySet().stream()
                .filter(k -> k.startsWith(header))
                .collect(Collectors.toMap(k -> k.split(header, 2)[1], headers::get));

        if (status == null && responseHeaders.isEmpty() && body == null)
            log.debug("{}. Response status, headers and body are not passed", SERVICE);

        if (status != null) {
            log.debug("{}. Set response status: {}", SERVICE, status);
            data.put("status", status);
        }
        if (!responseHeaders.isEmpty()) {
            responseHeaders.forEach((k, v) -> log.debug("{}. Set response header: {} --> {}", SERVICE, k, v));
            data.put("headers", responseHeaders);
        }
        if (body != null) {
            log.debug("{}. Set response body: {}", SERVICE, body);
            data.put("body", body);
        }
    }

    public boolean removeResponseData(String key) {
        String[] parts = key.split(KEY_DELIMITER, 2);
        return parts[1].equals(ALL)
                ? removeAllKeys(responseData, parts[0], teams, SERVICE)
                : removeOneKey(responseData, key, SERVICE);
    }
}
