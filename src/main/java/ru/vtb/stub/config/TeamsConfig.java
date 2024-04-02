package ru.vtb.stub.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.vtb.stub.db.entity.TeamEntity;
import ru.vtb.stub.db.repository.TeamRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "team")
@ConditionalOnProperty(value = "false", havingValue = "false")
public class TeamsConfig implements ApplicationListener<ApplicationReadyEvent> {

    private static List<String> codes;
    private TeamRepository teamRepository;

    public static List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        TeamsConfig.codes = codes;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        teamRepository = (TeamRepository) applicationReadyEvent.getApplicationContext()
                .getBean("teamRepository");

        List<TeamEntity> teamEntities = teamRepository.findAll();

        List<String> entityCodes = teamEntities.stream()
                .map(TeamEntity::getCode)
                .collect(Collectors.toList());

        // Добавление кодов команд, которые присутствуют в application.yaml и отсутствуют в БД
        getMissingElements(codes, entityCodes).forEach(this::insertTeamCode);

        // Удаление из БД кодов команд, которых нет в application.yaml
        getMissingElements(entityCodes, codes).forEach(this::deleteTeamCode);
    }

    public static boolean checkTeamCode(String code) {
        return codes.contains(code);
    }

    private void insertTeamCode(String code) {
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setCode(code);
        teamRepository.save(teamEntity);
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
