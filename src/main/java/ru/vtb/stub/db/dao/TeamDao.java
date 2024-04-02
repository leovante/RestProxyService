package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.TeamEntity;
import ru.vtb.stub.db.repository.EndpointRepository;
import ru.vtb.stub.db.repository.TeamRepository;
import ru.vtb.stub.domain.StubData;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamDao {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamEntity save(StubData data) {
        TeamEntity team = new TeamEntity();
        team.setCode(data.getTeam());
//        team.setEndpoints(List.of(endpoint));
//        endpoint.setTeam(team);

        return teamRepository.save(team);
    }

}
