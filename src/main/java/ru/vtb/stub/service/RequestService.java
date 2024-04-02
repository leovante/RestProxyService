package ru.vtb.stub.service;

import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;

import java.util.List;

public interface RequestService {

    void putData(StubData data);

    StubData getData(String key);

    StubData[] getTeamData(String team);

    StubData removeData(String key);

    void removeTeamData(String team);

    List<Request> getHistory(String key);

}
