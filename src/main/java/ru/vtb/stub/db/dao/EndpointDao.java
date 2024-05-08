package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.RequestHistoryEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.db.repository.RequestHistoryRepository;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.mapper.EntityToDtoMapper;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndpointDao {

    private final EndpointRepository endpointRepository;
    private final RequestHistoryRepository requestHistoryRepository;
    private final StubDataToEntityMapper stubDataToEntityMapper;
    private final EntityToDtoMapper entityToDtoMapper;

    @Transactional
    public EndpointEntity saveSingle(StubData data) {
        var endpoint = stubDataToEntityMapper.mapStubDataToEndpoint(data);
        Optional.ofNullable(endpoint.getResponses())
                .ifPresent(i -> {
                    i.forEach(it -> it.getHeaders().forEach(it2 -> it2.setResponse(i)));
                    i.forEach(it -> it.setEndpoint(endpoint));
                });

        requestHistoryRepository.removeByEndpointPk(endpoint.getPrimaryKey());
        endpointRepository.removeByPrimaryKey(endpoint.getPrimaryKey());

        return endpointRepository.save(endpoint);
    }

    @Transactional
    public Optional<EndpointEntity> getDataByPk(GetDataBaseRequest key) {
        var pk = stubDataToEntityMapper.mapBaseRequestToEndpointPathMethodTeamPk(key);
        return endpointRepository.findByPrimaryKey(pk);
    }

    @Transactional
    public Optional<EndpointEntity> getDataByPkAndMarkUsed(GetDataBaseRequest key) {
        var endpoint = getDataByPk(key);
        endpoint.ifPresent(e -> {
            if (e.getResponses().size() > 1) {
                var responses = e.getResponses().stream().sorted(Comparator.comparing(ResponseEntity::getCreatedAt)).toList();
                var first = responses.stream().filter(it -> !it.getIsUsed()).findFirst();
                if (first.isPresent()) {
                    first.get().setIsUsed(true);
                } else {
                    responses.forEach(it2 -> it2.setIsUsed(false));
                    responses.get(0).setIsUsed(true);
                }
                endpointRepository.save(e);
            }
        });
        return endpoint;
    }

    @Transactional
    public List<Request> getHistoryDataByPk(GetDataBaseRequest key) {
        return getDataByPk(key)
                .map(EndpointEntity::getRequestHistory)
                .map(it -> it.stream().map(RequestHistoryEntity::getRequest).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Transactional
    public List<StubData> getDataByTeam(String team) {
        return endpointRepository.findTop30ByTeam(team)
                .map(it -> it.stream()
                        .map(entityToDtoMapper::mapEntityToStubData)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Transactional
    public void removeByPk(GetDataBaseRequest key) {
        var pk = stubDataToEntityMapper.mapBaseRequestToEndpointPathMethodTeamPk(key);
        endpointRepository.removeByPrimaryKey(pk);
    }

    @Transactional
    public void removeByTeam(String team) {
        endpointRepository.removeByTeam(team);
    }

    @Transactional
    public Optional<EndpointEntity> getOneByTeam(String team) {
        return endpointRepository.findTop1ByTeam(team);
    }

}
