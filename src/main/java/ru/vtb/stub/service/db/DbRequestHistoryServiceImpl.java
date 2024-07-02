package ru.vtb.stub.service.db;

import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import ru.vtb.stub.db.dao.RequestHistoryDao;
import ru.vtb.stub.service.RequestHistoryService;

@RequiredArgsConstructor
@Singleton
public class DbRequestHistoryServiceImpl implements RequestHistoryService {

    private final RequestHistoryDao requestHistoryDao;

    public void saveRequest(String rpsRequest, String team, HttpRequest<?> httpRequest) {
        var path = rpsRequest.split(":")[0];
        var method = rpsRequest.split(":")[1];
        requestHistoryDao.save(path, method, team, httpRequest);
    }

}
