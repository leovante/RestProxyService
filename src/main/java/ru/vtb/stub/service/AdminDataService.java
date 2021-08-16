package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static ru.vtb.stub.data.ResponseData.responseData;

@Slf4j
@Service
public class AdminDataService {

    public Object getResponseData(String key) {
        var data = responseData.get(key);
        if (data != null)
            log.info("Admin data service. Get response data: {}", data);
        else
            log.info("Admin data service. No response data for key: {}", key);
        return data;
    }

    public void putResponseData(String key, Object body, Integer status) {
        if (!responseData.containsKey(key)) responseData.put(key, new HashMap<>());
        var data = responseData.get(key);
        if (body != null) {
            log.info("Admin data service. Set response body: {}", body);
            data.put("body", body);
        }
        if (status != null) {
            log.info("Admin data service. Set response status: {}", status);
            data.put("status", status);
        }
        if (body == null && status == null)
            log.info("Admin data service. Body and status not passed");
    }

    public Object removeResponseData(String key) {
        var data = responseData.remove(key);
        if (data != null)
            log.info("Admin data service. Delete response data: {}", data);
        else
            log.info("Admin data service. No response data for key: {}", key);
        return data;
    }
}
