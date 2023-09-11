package ru.vtb.stub.config;

import lombok.Setter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.vtb.stub.entity.Team;
import ru.vtb.stub.repository.TeamRepository;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Component
@ConfigurationProperties(prefix = "prefix")
public class InsertTeams implements ApplicationListener<ApplicationReadyEvent> {

    private List<String> teams;

    TeamRepository teamRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        teamRepository = (TeamRepository) applicationReadyEvent.getApplicationContext()
                .getBean("teamRepository");
        List<Team> teamEntityList = teamRepository.findAll();

        List<String> codes = teamEntityList.stream()
                .map(Team::getCode)
                .collect(Collectors.toList());

        // Добавление кодов команд, которые присутствуют в application.yaml и отсутствуют в БД
        getMissingElements(teams, codes).forEach(this::insertTeamCode);

        // Удаление из БД кодов команд, которых нет в application.yaml
        getMissingElements(codes, teams).forEach(this::deleteTeamCode);
    }

    private void insertTeamCode(String code) {
        Team team = new Team();
        team.setCode(code);
        teamRepository.save(team);
    }

    private void deleteTeamCode(String code) {
        teamRepository.deleteByCode(code);
    }

    private List<String> getMissingElements(List<String> list1, List<String> list2) {
        return list1.stream()
                .filter(el -> !list2.contains(el))
                .collect(Collectors.toList());
    }
}
