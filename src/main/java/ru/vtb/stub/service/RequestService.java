package ru.vtb.stub.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtb.stub.domain.DataBlock;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.StubData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.vtb.stub.data.ResponseData.*;

@Slf4j
@Service
public class RequestService {

    private static final String STATUS = "status";
    private static final String HEADERS = "headers";
    private static final String BODY = "body";

    public void putData(StubData requestData) {
        String key = "/" + requestData.getId() + requestData.getRoute() + ":" + requestData.getMethod();
        log.debug("key: " + key);

        if (requestData.getData() != null) putDataBlock(responseData, key, requestData.getData());
        if (requestData.getError() != null) putDataBlock(errorData, key, requestData.getError());
        if (requestData.getValidate() != null) putDataBlock(validateData, key, requestData.getValidate());
    }

    public StubData getData(String key) {
        StubData stubData = new StubData();
        if (responseData.get(key) != null) {
            stubData.setData(new DataBlock());
            getDataBlock(responseData.get(key), stubData.getData());
        }
        if (errorData.get(key) != null) {
            stubData.setError(new DataBlock());
            getDataBlock(errorData.get(key), stubData.getError());
        }
        if (validateData.get(key) != null) {
            stubData.setValidate(new DataBlock());
            getDataBlock(validateData.get(key), stubData.getValidate());
        }
        if (stubData.getData() != null || stubData.getError() != null || stubData.getValidate() != null) {
            Matcher matcher = Pattern.compile("^/([^/]+)([^:]+):(.+)$").matcher(key);
            if (matcher.find()) {
                stubData.setId(matcher.group(1));
                stubData.setRoute(matcher.group(2));
                stubData.setMethod(matcher.group(3));
            }
        }
        return stubData;
    }

    public void removeAllData(String key) {
        if (responseData.get(key) != null) responseData.remove(key);
        if (errorData.get(key) != null) errorData.remove(key);
        if (validateData.get(key) != null) validateData.remove(key);
    }

    private void putDataBlock(Map<String, Map<String, Object>> map, String key, DataBlock data) {
        var stubData = map.get(key) == null ? new HashMap<String, Object>() : map.get(key);
        map.put(key, stubData);
        if (data.getStatus() != null) stubData.put(STATUS, data.getStatus());
        if (!data.getHeaders().isEmpty()) stubData.put(HEADERS, data.getHeaders());
        if (data.getBody() != null) stubData.put(BODY, data.getBody());
    }

    private void getDataBlock(Map<String, Object> map, DataBlock data) {
        data.setStatus((Integer) map.get(STATUS));
        data.setHeaders((List<Header>) map.get(HEADERS));
        data.setBody((JsonNode) map.get(BODY));
    }
}
