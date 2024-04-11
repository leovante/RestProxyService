package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.HeaderEntity;
import ru.vtb.stub.db.entity.TeamEntity;
import ru.vtb.stub.db.repository.HeaderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeaderDao {

    private final HeaderRepository headerRepository;

    /*@Transactional
    public TeamEntity save(List<HeaderEntity> data) {
        TeamEntity team = new TeamEntity();
        team.setCode(data.getTeam());

        return headerRepository.findByPrimaryKey(data.get)
                .orElseGet(() -> headerRepository.save(team));
        return null;
    }*/

}
