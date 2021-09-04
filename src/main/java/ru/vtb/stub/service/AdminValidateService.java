package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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

    public void putValidateData(String key, HttpServletRequest request) {
        if (!validateData.containsKey(key)) validateData.put(key, new HashMap<>());
        var data = validateData.get(key);

        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();

            if (header.startsWith("query-")) {
                log.info("Admin validate service. Set expected query param: {} --> {}", header, request.getHeader(header));
                data.put(header, request.getHeader(header));
            }

            if (header.startsWith("header-")) {
                log.info("Admin validate service. Set expected header: {} --> {}", header, request.getHeader(header));
                data.put(header, request.getHeader(header));
            }
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
