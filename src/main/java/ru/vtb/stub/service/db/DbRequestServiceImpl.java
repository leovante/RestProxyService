package ru.vtb.stub.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.dao.EndpointDao;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.service.mapper.EntityToDtoMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rest-proxy-stub.storage-mode", havingValue = "db")
public class DbRequestServiceImpl implements RequestService {

    private final EndpointDao endpointDao;
    private final EntityToDtoMapper entityToDtoMapper;

    @Override
    public void putData(StubData data) {
        endpointDao.saveSingle(data);
    }

    @Override
    @Transactional
    public StubData getData(GetDataBaseRequest key) {
        return endpointDao.getDataByPk(key)
                .map(entityToDtoMapper::mapEntityToStubData)
                .orElse(null);
    }

    @Override
    @Transactional
    public StubData getDataByPkAndMarkUsed(GetDataBaseRequest key) {
        return endpointDao.getDataByPkAndMarkUsed(key)
                .map(entityToDtoMapper::mapEntityToStubData)
                .orElse(null);
    }

    @Override
    public StubData[] getTeamData(String team) {
        var data = endpointDao.getDataByTeam(team);
        return data.toArray(StubData[]::new);
    }

    @Override
    public void removeData(GetDataBaseRequest key) {
        endpointDao.removeByPk(key);
    }

    @Override
    public void removeTeamData(String team) {
        endpointDao.removeByTeam(team);
    }

    @Override
    public List<Request> getHistory(GetDataBaseRequest key) {
        return endpointDao.getHistoryDataByPk(key);
    }

}
