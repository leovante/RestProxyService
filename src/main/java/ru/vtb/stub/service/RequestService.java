package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtb.stub.domain.StubData;

import static ru.vtb.stub.data.ResponseData.dataMap;

@Slf4j
@Service
public class RequestService {

    public void putData(StubData data) {
        String key = data.getRoute() + ":" + data.getMethod();
        log.debug("Put data: {} --> {}", key, data);
        dataMap.put(key, data);
    }

    public StubData getData(String key) {
        var data = dataMap.get(key);
        log.debug("Getting data: {} --> {}", key, data);
        return data;
    }

    public void removeAllData(String key) {
        var data = dataMap.remove(key);
        log.debug("Deleting data: {} --> {}", key, data);
    }
}
