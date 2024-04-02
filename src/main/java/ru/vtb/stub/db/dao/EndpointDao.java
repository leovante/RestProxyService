package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.TeamEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.domain.StubData;

@Service
@RequiredArgsConstructor
public class EndpointDao {

    private final EndpointRepository endpointRepository;

    @Transactional
    public EndpointEntity save(StubData data, TeamEntity team) {
        EndpointEntity endpoint = new EndpointEntity();
        endpoint.setPath(data.getPath());
        endpoint.setMethod(data.getMethod());
        endpoint.setWait(data.getWait());
        endpoint.setTeam(team);

        return endpointRepository.save(endpoint);
    }

}
