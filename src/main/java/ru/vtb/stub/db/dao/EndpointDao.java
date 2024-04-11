package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.repository.EndpointRepository;

@Service
@RequiredArgsConstructor
public class EndpointDao {

    private final EndpointRepository endpointRepository;

    @Transactional
    public EndpointEntity save(EndpointEntity endpoint) {
        return endpointRepository.findByPrimaryKey(endpoint.getPrimaryKey())
                .orElseGet(() -> endpointRepository.save(endpoint));
    }

}
