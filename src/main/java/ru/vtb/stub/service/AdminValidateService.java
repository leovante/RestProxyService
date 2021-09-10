package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONTokener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static ru.vtb.stub.data.ResponseData.validateData;

@Slf4j
@Service
public class AdminValidateService {

    public Object getValidateData(String key) {
        var data = validateData.get(key);
        if (data != null)
            log.info("Admin validate service. Get validate data: {}", data);
        else
            log.info("Admin validate service. No validate data for key: {}", key);
        return data;
    }

    public void putValidateData(String key, Map<String, String> headers, String body) {
        if (!validateData.containsKey(key)) validateData.put(key, new HashMap<>());
        var data = validateData.get(key);

        headers.forEach((k, v) -> {
            if (k.startsWith("query-") || k.startsWith("header-")) {
                log.info("Admin validate service. Set expected " + k.split("-", 2)[0] + ": {} --> {}", k, v);
                data.put(k, v);
            }
        });
        if (body != null) {
            try {
                new JSONTokener(body);
            } catch (JSONException e) {
                log.info("Admin validate service. Body is not a JSON: {}", body);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
            log.info("Admin validate service. Set expected json schema: {}", body);
            data.put("body", body);
        }

        if (data.isEmpty()) {
            log.info("Admin validate service. Data does not contain body and not headers starts with 'query-' or 'header-'");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Validate data does not contain body and not headers starts with 'query-' or 'header-'");
        }
    }

    public Object removeValidateData(String key) {
        var data = validateData.remove(key);
        if (data != null)
            log.info("Admin validate service. Delete validate data: {}", data);
        else
            log.info("Admin validate service. No validate data for key: {}", key);
        return data;
    }
}
