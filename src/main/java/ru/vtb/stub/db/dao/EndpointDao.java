package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.RequestHistoryEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.db.repository.RequestHistoryRepository;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.mapper.EntityToDtoMapper;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    public StubData getDataByPkAndSaveIdx(GetDataBaseRequest key) {
        var endpoint = getDataByPk(key);
        StubData response = entityToDtoMapper.mapEntityToStubData(endpoint.orElse(null));
        endpoint.ifPresent(entityEndpoint -> {
            if (entityEndpoint.getResponses().size() > 1) {
                var responses = entityEndpoint.getResponses();
                var first = responses.stream().filter(json -> Objects.equals(json.getIdx(), entityEndpoint.getIdx())).findFirst();
                if (first.isPresent()) {
                    if (responses.size() - 1 < entityEndpoint.getIdx()) {
                        entityEndpoint.setIdx(first.get().getIdx() + 1);
                    } else if (responses.size() - 1 == entityEndpoint.getIdx()) {
                        entityEndpoint.setIdx(0);
                    }
                    response.setResponse(first.get());
                } /*else {
                    responses.forEach(it2 -> it2.setIsUsed(false));
                    responses.get(0).setIsUsed(true);
                }*/
                endpointRepository.save(entityEndpoint);
            }
        });
        return response;
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
