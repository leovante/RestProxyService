package ru.vtb.stub.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.vtb.stub.db.dao.EndpointDao;
import ru.vtb.stub.db.dao.ResponseDao;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.RequestService;

import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rest-proxy-stub.storage-mode", havingValue = "db")
public class DbRequestServiceImpl implements RequestService {

    private final ResponseDao responseDao;
    private final EndpointDao endpointDao;

    @Override
    public void putData(StubData data) {
        responseDao.saveSingle(data);
    }

    @Override
    public StubData getData(GetDataBaseRequest key) {
        return endpointDao.getDataByPk(key);
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
//        return endpointDao.getHistory();
        return null;
    }

}
