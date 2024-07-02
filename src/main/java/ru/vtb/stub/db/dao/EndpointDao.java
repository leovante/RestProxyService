package ru.vtb.stub.db.dao;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.db.repository.RequestHistoryRepository;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.mapper.EntityToDtoMapper;
import ru.vtb.stub.service.mapper.MapperUtils;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Singleton
public class EndpointDao {

    private final RequestHistoryRepository requestHistoryRepository;
    private final EndpointRepository endpointRepository;
    private final StubDataToEntityMapper stubDataToEntityMapper;
    private final EntityToDtoMapper entityToDtoMapper;

    @Transactional
    public EndpointEntity saveSingle(StubData data) {
        var endpoint = stubDataToEntityMapper.mapStubDataToEndpoint(data);
        var sId = endpoint.getSecondId();

        var endpoints = getEndpointListByPk(sId.getTeam(), sId.getPath(), sId.getMethod(), endpoint.getIsRegex());
        if (endpoints != null && !endpoints.isEmpty()) {
            remove(endpoints);
        }

        return endpointRepository.save(endpoint);
    }

    public Optional<EndpointEntity> getEndpointByPk(GetDataBaseRequest key) {
        return getEndpointByPk(key.getTeam(), key.getPath(), key.getMethod());
    }

    @Transactional
    public Optional<EndpointEntity> getEndpointByPk(String team, String path, String method) {
        if (path.contains("--")) {
            return endpointRepository.findByTeamAndRegexPathAndMethod(team, MapperUtils.buildRegexKey(path), method)
                    .stream().reduce((x, y) -> y);
        } else {
            return endpointRepository.findByTeamAndNormalPathAndMethod(team, path, method);
        }
    }

    @Transactional
    public List<EndpointEntity> getEndpointListByPk(String team, String path, String method, Boolean isRegex) {
        if (isRegex) {
            return endpointRepository.findByTeamAndRegexPathAndMethod(team, MapperUtils.buildRegexKey(path), method);
        } else {
            return endpointRepository.findByTeamAndNormalPathAndMethod(team, MapperUtils.buildRegexKey(path), method)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
    }

    @Transactional
    public Optional<EndpointEntity> getEndpointByPkAndMarkUsed(GetDataBaseRequest key) {
        var endpoint = getEndpointByPk(key);
        endpoint.ifPresent(e -> {
            if (e.getResponses().size() > 1) {
                var responses = e.getResponses().stream().sorted(Comparator.comparing(ResponseEntity::getCreatedAt)).toList();
                var first = responses.stream().filter(it -> !it.getIsUsed()).findFirst();
                if (first.isPresent()) {
                    first.get().setIsUsed(true);
                    first.get().setCreatedAt(Instant.now());
                } else {
                    responses.forEach(it -> {
                        it.setIsUsed(false);
                        it.setCreatedAt(Instant.now());
                    });
                    responses.get(0).setIsUsed(true);
                }
                endpointRepository.update(e);
            }
        });
        return endpoint;
    }

    @Transactional
    public List<StubData> getEndpointByTeam(String team) {
        return endpointRepository.findTop30ByTeam(team).stream()
                .map(entityToDtoMapper::mapEntityToStubData)
                .collect(Collectors.toList());
    }

    public void remove(List<EndpointEntity> endpoints) {
        endpointRepository.deleteAll(endpoints);
        requestHistoryRepository.deleteByEndpoints(endpoints.stream().map(EndpointEntity::getId).toList());
    }

    @Transactional
    public void remove(String team, String path, String method) {
        if (path.contains("--")) {
            endpointRepository.deleteByTeamAndRegexPathAndMethod(team, MapperUtils.buildRegexKey(path), method);
        } else {
            var find = endpointRepository.findByTeamAndNormalPathAndMethod(team, path, method);
            find.ifPresent(endpointRepository::delete);
        }
    }

    @Transactional
    public void removeByTeam(String team) {
        endpointRepository.deleteByTeam(team);
        requestHistoryRepository.deleteByTeam(team);
    }

    @Transactional
    public Optional<EndpointEntity> getOneByTeam(String team) {
        if (team == null) {
            return Optional.empty();
        }
        return endpointRepository.findTop1ByTeam(team);
    }

}
