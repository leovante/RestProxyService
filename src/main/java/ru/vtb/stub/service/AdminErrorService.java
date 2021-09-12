package ru.vtb.stub.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

import static ru.vtb.stub.data.ResponseData.errorData;
import static ru.vtb.stub.utils.CommonUtils.*;

@Slf4j
@Setter
@Service
@ConfigurationProperties(prefix = "prefix")
public class AdminErrorService {

    private Set<String> teams;
    private static final String SERVICE = "Admin error service";

    public Object getErrorData(String key) {
        return getData(errorData, key, SERVICE);
    }

    public void putErrorData(String key, String message, Integer status) {
        if (!errorData.containsKey(key)) errorData.put(key, new HashMap<>());
        var error = errorData.get(key);

        if (status == null && (message == null || message.isEmpty()))
            log.debug("{}. Status and message not passed", SERVICE);

        if (status != null) {
            log.debug("{}. Set status --> {}", SERVICE, status);
            error.put("status", status);
        } else {
            log.debug("{}. Set default status --> 500", SERVICE);
            error.put("status", 500);
        }
        if (message != null && !message.isEmpty()) {
            log.debug("{}. Set message --> {}", SERVICE, message);
            error.put("message", message);
        }
    }

    public boolean removeErrorData(String key) {
        String[] parts = key.split(KEY_DELIMITER, 2);
        return parts[1].equals(ALL)
                ? removeAllKeys(errorData, parts[0], teams, SERVICE)
                : removeOneKey(errorData, key, SERVICE);
    }
}
