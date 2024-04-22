package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.RequestHistoryEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.mapper.EntityToDtoMapper;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndpointDao {

    private static final String TEMPLATE = "--";

    private final EndpointRepository endpointRepository;
    private final StubDataToEntityMapper stubDataToEntityMapper;
    private final EntityToDtoMapper entityToDtoMapper;

    @Transactional
    public EndpointEntity saveSingle(StubData data) {
        var endpoint = stubDataToEntityMapper.mapStubDataToEndpoint(data);
        var responses = endpoint.getResponses();
        responses.forEach(it -> it.getHeaders().forEach(it2 -> it2.setResponse(responses)));
        responses.forEach(it -> it.setEndpoint(endpoint));

        endpointRepository.removeAllByPrimaryKey(endpoint.getPrimaryKey());
        return endpointRepository.save(endpoint);
    }

    @Transactional
    public Optional<EndpointEntity> getDataByPk(GetDataBaseRequest key) {
        var pk = stubDataToEntityMapper.mapBaseRequestToEndpointPathMethodTeamPk(key);
        return endpointRepository.findByPrimaryKey(pk);
    }

    @Transactional
    public List<Request> getHistoryDataByPk(GetDataBaseRequest key) {
        var pk = stubDataToEntityMapper.mapBaseRequestToEndpointPathMethodTeamPk(key);

        Optional<EndpointEntity> res;
        if (pk.getPath().contains(TEMPLATE)) {
            res = endpointRepository.findRegexByTeamAndPathAndMethod(key.getTeam(), key.getPath(), RequestMethod.valueOf(key.getMethod()));
        } else {
            res = Optional.ofNullable(endpointRepository.findByPrimaryKey(pk)
                    .orElseGet(() ->
                            endpointRepository.findRegexByTeamAndPathAndMethod(key.getTeam(), key.getPath(), RequestMethod.valueOf(key.getMethod()))
                                    .orElse(null)
                    ));
        }
        return res
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
        endpointRepository.removeAllByPrimaryKey(pk);
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
