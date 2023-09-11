package ru.vtb.stub.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.entity.Endpoint;
import ru.vtb.stub.entity.Header;
import ru.vtb.stub.entity.Response;
import ru.vtb.stub.entity.Team;
import ru.vtb.stub.repository.EndpointRepository;
import ru.vtb.stub.repository.HeaderRepository;
import ru.vtb.stub.repository.ResponseRepository;
import ru.vtb.stub.repository.TeamRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.vtb.stub.data.DataMap.*;

@Slf4j
@Service
public class RequestService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EndpointRepository endpointRepository;
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private HeaderRepository headerRepository;

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




        Team team = new Team();
        team.setCode(data.getTeam());
        teamRepository.save(team);

        Endpoint endpoint = new Endpoint();
        endpoint.setPath(data.getPath());
        endpoint.setMethod(data.getMethod());
        endpoint.setWait(data.getWait());
        endpoint.setTeam(team);
        endpointRepository.save(endpoint);

        Response response = new Response();
        response.setStatus(data.getResponse().getStatus());
        response.setBodyJson(data.getResponse().getBody().asText());
        response.setEndpoint(endpoint);
        responseRepository.save(response);

        Set<Header> headers = new HashSet<>();
        if (Objects.nonNull(data.getResponse().getHeaders())) {
            headers = data.getResponse().getHeaders().stream()
                    .map(h -> addHeaderEntity(h, response))
                    .collect(Collectors.toSet());
        }
        headerRepository.saveAll(headers);
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

    // For JPA
    private Header addHeaderEntity(ru.vtb.stub.domain.Header headerDto, Response response) {
        Header headerEntity = new Header();
        headerEntity.setName(headerDto.getName());
        headerEntity.setValue(headerDto.getValue());
        headerEntity.setResponse(response);
        return headerEntity;
    }
}
