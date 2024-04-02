package ru.vtb.stub.service.inMemoryStorage;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.HeaderEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.db.entity.TeamEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.db.repository.HeaderRepository;
import ru.vtb.stub.db.repository.ResponseRepository;
import ru.vtb.stub.db.repository.TeamRepository;
import ru.vtb.stub.service.RequestService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.vtb.stub.config.TeamsConfig.getCodes;
import static ru.vtb.stub.data.DataMap.*;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rest-proxy-stub.storage-mode", havingValue = "ram")
public class RequestServiceImpl implements RequestService {

    private final TeamRepository teamRepository;
    private final EndpointRepository endpointRepository;
    private final ResponseRepository responseRepository;
    private final HeaderRepository headerRepository;

    private static final String TEMPLATE = "--";

    @Override
    public void putData(StubData data) {
        String team = data.getTeam();

        checkTeam(team);

        String path = data.getPath();
        String method = data.getMethod();
        String teamPrefix = "/" + team;

        String key = teamPrefix + path + ":" + method;
        log.debug("Put data: {} --> {}", key, data);

        // Если path содержит "--", то "--" заменяются на regex
        String actualPath = path.contains(TEMPLATE)
                ? teamPrefix + buildRegexKey(path)
                : teamPrefix + path;

        // Если для этого ключа (path+method) уже есть запись - она удаляется
        EndpointEntity existEndpoint = endpointRepository.findByPathAndMethod(actualPath, method);
        if(Objects.nonNull(existEndpoint)) {
            endpointRepository.delete(existEndpoint);
        }

        TeamEntity teamEntity = teamRepository.findByCode(team);

        EndpointEntity endpointEntity = new EndpointEntity();
        endpointEntity.setPath(actualPath);
        endpointEntity.setMethod(method);
        endpointEntity.setWait(data.getWait());
        endpointEntity.setTeam(teamEntity);
        endpointRepository.save(endpointEntity);

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setStatus(data.getResponse().getStatus());
        responseEntity.setBody(data.getResponse().getBody());
        responseEntity.setEndpoint(endpointEntity);
        responseRepository.save(responseEntity);

        Set<HeaderEntity> headerEntities = new HashSet<>();
        if (Objects.nonNull(data.getResponse().getHeaders())) {
            headerEntities = data.getResponse().getHeaders().stream()
                    .map(h -> addHeaderEntity(h, responseEntity))
                    .collect(Collectors.toSet());
        }
        headerRepository.saveAll(headerEntities);

        // TODO - delete history
//        List<Request> requests = requestMap.remove(key);
//        if (!ObjectUtils.isEmpty(requests)) {
//            log.debug("Deleted history: {} --> {}", key, requests);
//        }
    }

    @Override
    public StubData getData(String key) {
        StubData data = key.contains(TEMPLATE) ? dataByRegexMap.get(buildRegexKey(key)) : dataByKeyMap.get(key);
        log.debug("Get data: {} --> {}", key, data);
        return data;
    }

    @Override
    public StubData[] getTeamData(String team) {
        List<StubData> data = new ArrayList<>();
        Stream.of(getTeamValues(dataByKeyMap, team), getTeamValues(dataByRegexMap, team))
                .forEach(data::addAll);
        log.debug("Get data: {} --> {}", team, data);
        return data.toArray(StubData[]::new);
    }

    @Override
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

    @Override
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

    @Override
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

    @SneakyThrows
    private void checkTeam(String team) {
        if (!getCodes().contains(team)) {
            throw new Exception("Team codes not contains: " + team);
        }
    }

    private HeaderEntity addHeaderEntity(ru.vtb.stub.domain.Header headerDto, ResponseEntity responseEntity) {
        HeaderEntity headerEntity = new HeaderEntity();
        headerEntity.setName(headerDto.getName());
        headerEntity.setValue(headerDto.getValue());
        headerEntity.setResponse(responseEntity);
        return headerEntity;
    }
}
