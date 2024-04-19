package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
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

    private final EndpointRepository endpointRepository;
    private final StubDataToEntityMapper stubDataToEntityMapper;
    private final EntityToDtoMapper entityToDtoMapper;

    @Transactional
    public StubData getDataByPk(GetDataBaseRequest key) {
        var pk = stubDataToEntityMapper.mapBaseRequestToEndpointPathMethodTeamPk(key);

        return endpointRepository.findByPrimaryKey(pk)
                .map(entityToDtoMapper::mapEntityToStubData)
                .orElse(null);
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
    public Optional<EndpointEntity> getOneByTeam(String team){
        return endpointRepository.findTop1ByTeam(team);
    }

}
