package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.vtb.stub.data.DataMap.*;

@Slf4j
@Service
public class RequestService {

    private static final String TEMPLATE = "--";

    public void putData(StubData data) {
        String key = "/" + data.getTeam() + data.getPath() + ":" + data.getMethod();

        log.debug("Put data: {} --> {}", key, data);

        if (key.contains(TEMPLATE)) {
            key = buildRegexKey(key);
            dataByRegexMap.put(key, data);
        }
        else {
            dataByKeyMap.put(key, data);
        }

        List<Request> requests = requestMap.remove(key);
        if (!ObjectUtils.isEmpty(requests)) {
            log.debug("Deleted history: {} --> {}", key, requests);
        }
    }

    public StubData getData(String key) {
        StubData data = key.contains(TEMPLATE) ? dataByRegexMap.get(buildRegexKey(key)) : dataByKeyMap.get(key);
        log.debug("Get data: {} --> {}", key, data);
        return data;
    }

    public StubData[] getTeamData(String team) {
        List<StubData> data = new ArrayList<>();
        Stream.of(getTeamValues(dataByKeyMap, team), getTeamValues(dataByRegexMap, team))
                .forEach(data::addAll);
        log.debug("Get data: {} --> {}", team, data);
        return data.toArray(StubData[]::new);
    }

    public StubData removeData(String key) {
        StubData data;
        if (key.contains(TEMPLATE)) {
            key = buildRegexKey(key);
            data = dataByRegexMap.remove(key);
        }
        else {
            data = dataByKeyMap.remove(key);
        }

        log.debug("Deleted data: {} --> {}", key, data);

        List<Request> requests = requestMap.remove(key);
        if (!ObjectUtils.isEmpty(requests)) {
            log.debug("Deleted history: {} --> {}", key, requests);
        }

        return data;
    }

    public void removeTeamData(String team) {
        List<String> keys = getTeamKeys(dataByKeyMap, team);
        List<String> regexKeys = getTeamKeys(dataByRegexMap, team);
        if (!keys.isEmpty() || !regexKeys.isEmpty()) {
            log.debug("Start deleting team '{}' data...", team);
        }

        removeTeamValues(dataByKeyMap, keys);
        removeTeamValues(dataByRegexMap, regexKeys);

        List<String> requests = getTeamKeys(requestMap, team);
        if (!requests.isEmpty()) {
            requests.forEach(k -> requestMap.remove(k));
            log.debug("Deleted all history for: {}", team);
        }
    }

    public List<Request> getHistory(String key) {
        if (key.contains(TEMPLATE)) {
            key = buildRegexKey(key);
        }

        List<Request> requests = requestMap.get(key);
        log.debug("Get history: {} --> {}", key, requests);
        return ObjectUtils.isEmpty(requests) ? new ArrayList<>() : requests;
    }

    private List<String> getTeamKeys(Map<String, ?> map, String team) {
        return map.keySet().stream()
                .filter(k -> k.startsWith("/" + team))
                .collect(Collectors.toList());
    }

    private List<StubData> getTeamValues(Map<String, StubData> map, String team) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().startsWith("/" + team))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void removeTeamValues(Map<String, StubData> map, List<String> keys) {
        keys.forEach(k -> {
            StubData data = map.remove(k);
            log.debug("Deleted data: {} --> {}", k, data);
        });
    }

    private String buildRegexKey(String key) {
        return key.replaceAll(TEMPLATE, "[a-zA-Z0-9.@%/_-]+")
                .replaceAll("/", "\\/") + "$";
    }
}
