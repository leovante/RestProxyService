package ru.vtb.stub.db.dao;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.service.mapper.StubDataToEntityMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseDao {

    private final StubDataToEntityMapper stubDataToEntityMapper;
    private final EntityManager em;

    @Transactional
    public ResponseEntity saveSingle(StubData data) {
        var response = stubDataToEntityMapper.mapStubDataToEntity(data);
        var endpoint = response.getEndpoint();
        var headers = response.getHeaders();
        headers.forEach(it -> it.setResponse(List.of(response)));
        endpoint.setResponses(List.of(response));

        em.remove(response);
        var resp = em.merge(response);
        em.close();
        return resp;
    }

}
