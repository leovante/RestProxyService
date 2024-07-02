package ru.vtb.stub.service;

import io.micronaut.http.HttpRequest;

public interface RequestHistoryService {

    void saveRequest(String rpsRequest, String rpsKey, HttpRequest<?> servletRequest);

}
