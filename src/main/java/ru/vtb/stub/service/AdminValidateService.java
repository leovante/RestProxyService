package ru.vtb.stub.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONTokener;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ru.vtb.stub.data.ResponseData.validateData;
import static ru.vtb.stub.utils.CommonUtils.*;

@Slf4j
@Setter
@Service
@ConfigurationProperties(prefix = "prefix")
public class AdminValidateService {
    
    private String header;
    private String query;
    private Set<String> teams;

    private static final String SERVICE = "Admin validate service";

    public Object getValidateData(String key) {
        return getData(validateData, key, SERVICE);
    }

    public void putValidateData(String key, Map<String, String> headers, String body) {
        if (!validateData.containsKey(key)) validateData.put(key, new HashMap<>());
        var data = validateData.get(key);

        headers.forEach((k, v) -> {
            if (k.startsWith(query)) {
                log.debug("{}. Set query --> {}: {}", SERVICE, k, v);
                data.put(k, v);
            }
        });
        headers.forEach((k, v) -> {
            if (k.startsWith(header)) {
                log.debug("{}. Set header --> {}: {}", SERVICE, k, v);
                data.put(k, v);
            }
        });
        if (body != null) {
            try {
                new JSONTokener(body);
            } catch (JSONException e) {
                log.debug("{}. Body is not a JSON: {}", SERVICE, body);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
            log.debug("{}. Set json schema --> {}", SERVICE, body);
            data.put("body", body);
        }

        if (data.isEmpty()) {
            log.error("{}. Data does not contain body and not headers starts with '{}' or '{}'", query, header, SERVICE);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Validate data does not contain body and not headers starts with '" + query + "' or '" + header + "'");
        }
    }

    public boolean removeValidateData(String key) {
        String[] parts = key.split(KEY_DELIMITER, 2);
        return parts[1].equals(ALL)
                ? removeAllKeys(validateData, parts[0], teams, SERVICE)
                : removeOneKey(validateData, key, SERVICE);
    }
}
