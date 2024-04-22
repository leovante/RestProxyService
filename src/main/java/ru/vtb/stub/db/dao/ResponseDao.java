package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.db.repository.ResponseRepository;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseDao {

    private final EndpointRepository endpointRepository;
    private final ResponseRepository responseRepository;
    private final StubDataToEntityMapper stubDataToEntityMapper;

    @Transactional
    public List<ResponseEntity> getDataByEndpointPk(GetDataBaseRequest key) {
        var pk = stubDataToEntityMapper.mapBaseRequestToEndpointPathMethodTeamPk(key);
        return responseRepository.findByTeamAndPathAndMethod(pk.getTeam(), pk.getPath(), pk.getMethod())
                .filter(it -> !it.isEmpty())
                .orElseGet(() ->
                        endpointRepository.findRegexByTeamAndPathAndMethod(key.getTeam(), key.getPath(), RequestMethod.valueOf(key.getMethod()))
                                .map(EndpointEntity::getResponses)
                                .orElse(Collections.emptyList())
                );
    }

}
