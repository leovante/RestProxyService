package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.db.repository.HeaderRepository;
import ru.vtb.stub.db.repository.ResponseRepository;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

@Service
@RequiredArgsConstructor
public class ResponseDao {

    private final HeaderRepository headerRepository;
    private final EndpointDao endpointDao;
    private final TeamDao teamDao;
    private final ResponseRepository responseRepository;

    private final StubDataToEntityMapper stubDataToEntityMapper;

    @Transactional
    public ResponseEntity saveSingle(StubData data) {
        var team = teamDao.save(data);
        var endpoint = endpointDao.save(data, team);

        var responseSnapshot = stubDataToEntityMapper.mapResponseDtoToEntity(data.getResponse());
        responseSnapshot.setEndpoint(endpoint);
        responseSnapshot.getHeaders().forEach(it -> it.setResponse(responseSnapshot));
        return responseRepository.save(responseSnapshot);
    }

}
