package ru.vtb.stub.service.db;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Requires;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import ru.vtb.stub.db.dao.EndpointDao;
import ru.vtb.stub.db.dao.RequestHistoryDao;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.service.mapper.EntityToDtoMapper;

import java.util.List;

@RequiredArgsConstructor
@Requires(property = "rest-proxy-stub.storage-mode", value = "db")
@Singleton
public class DbRequestServiceImpl implements RequestService {

    private final EndpointDao endpointDao;
    private final RequestHistoryDao requestHistoryDao;
    private final EntityToDtoMapper entityToDtoMapper;

    @Override
    public void putData(StubData data) {
        endpointDao.saveSingle(data);
    }

    @Override
    @Transactional
    public List<StubData> getData(GetDataBaseRequest key) {
        return endpointDao.getEndpointListByPk(key.getTeam(), key.getPath(), key.getMethod(), key.getPath().contains("--")).stream()
                .map(entityToDtoMapper::mapEntityToStubData)
                .toList();
    }

    @Override
    @Transactional
    public StubData getDataByPkAndMarkUsed(GetDataBaseRequest key) {
        return endpointDao.getEndpointByPkAndMarkUsed(key)
                .map(entityToDtoMapper::mapEntityToStubData)
                .orElse(null);
    }

    @Override
    public StubData[] getTeamData(String team) {
        var data = endpointDao.getEndpointByTeam(team);
        return data.toArray(StubData[]::new);
    }

    @Override
    public void removeData(GetDataBaseRequest key) {
        endpointDao.remove(key.getTeam(), key.getPath(), key.getMethod());
    }

    @Override
    public void removeTeamData(String team) {
        endpointDao.removeByTeam(team);
    }

    @Override
    public List<Request> getHistory(GetDataBaseRequest key) {
        return requestHistoryDao.getHistoryRequestByPk(key);
    }

}
