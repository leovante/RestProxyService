package ru.vtb.stub.service;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;

public interface ResponseService {

    HttpResponse<?> sendResponse(String rpsRequest, String key, HttpRequest servletRequest);

}
