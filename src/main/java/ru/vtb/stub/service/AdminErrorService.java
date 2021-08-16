package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static ru.vtb.stub.data.ResponseData.errorData;

@Slf4j
@Service
public class AdminErrorService {

    public Object getErrorData(String key) {
        var data = errorData.get(key);
        if (data != null)
            log.info("Admin error service. Get data: {}", data);
        else
            log.info("Admin error service. No data for key: {}", key);
        return data;
    }

    public void putErrorData(String key, String message, Integer status) {
        if (!errorData.containsKey(key)) errorData.put(key, new HashMap<>());
        var error = errorData.get(key);
        if (status != null) {
            log.info("Admin error service. Set error status: {}", status);
            error.put("status", status);
        } else {
            log.info("Admin error service. Set default error status: 500");
            error.put("status", 500);
        }
        if (message != null && !message.isEmpty()) {
            log.info("Admin error service. Set error message: {}", message);
            error.put("message", message);
        }
        if (status == null && (message == null || message.isEmpty()))
            log.info("Admin error service. Status and message not passed");
    }

    public Object removeErrorData(String key) {
        var data = errorData.remove(key);
        if (data != null)
            log.info("Admin error service. Delete data: {}", data);
        else
            log.info("Admin error service. No data for key: {}", key);
        return data;
    }
}
