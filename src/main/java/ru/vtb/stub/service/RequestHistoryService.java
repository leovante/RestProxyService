package ru.vtb.stub.service;

import ru.vtb.stub.filter.RequestWrapper;

public interface RequestHistoryService {

    void saveRequest(String rpsRequest, String rpsKey, RequestWrapper servletRequest);

}
