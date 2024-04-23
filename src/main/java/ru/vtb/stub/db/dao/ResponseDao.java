package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.db.repository.ResponseRepository;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseDao {

    private final ResponseRepository responseRepository;
    private final StubDataToEntityMapper stubDataToEntityMapper;

    @Transactional
    public List<ResponseEntity> getDataByEndpointPk(GetDataBaseRequest key) {
        var pk = stubDataToEntityMapper.mapBaseRequestToEndpointPathMethodTeamPk(key);
        return responseRepository.findByTeamAndPathAndMethod(pk);
    }

}
