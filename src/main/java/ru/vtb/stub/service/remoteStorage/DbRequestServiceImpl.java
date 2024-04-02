package ru.vtb.stub.service.remoteStorage;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.vtb.stub.db.dao.ResponseDao;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.service.RequestService;

import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rest-proxy-stub.storage-mode", havingValue = "db")
public class DbRequestServiceImpl implements RequestService {

    private final ResponseDao responseDao;

    @Override
    public void putData(StubData data) {
        responseDao.saveSingle(data);
    }

    @Override
    public StubData getData(String key) {
        return null;
    }

    @Override
    public StubData[] getTeamData(String team) {
        return new StubData[0];
    }

    @Override
    public StubData removeData(String key) {
        return null;
    }

    @Override
    public void removeTeamData(String team) {

    }

    @Override
    public List<Request> getHistory(String key) {
        return null;
    }

}
