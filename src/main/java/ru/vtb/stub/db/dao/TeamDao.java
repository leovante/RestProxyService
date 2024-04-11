package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.TeamEntity;
import ru.vtb.stub.db.repository.TeamRepository;
import ru.vtb.stub.domain.StubData;

@Service
@RequiredArgsConstructor
public class TeamDao {

    private final TeamRepository teamRepository;

    @Transactional
    public TeamEntity save(StubData data) {
        TeamEntity team = new TeamEntity();
        team.setCode(data.getTeam());

        return teamRepository.findByCode(data.getTeam())
                .orElseGet(() -> teamRepository.save(team));
    }

}
