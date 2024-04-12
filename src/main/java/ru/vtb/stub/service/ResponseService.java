package ru.vtb.stub.service;

import org.springframework.http.ResponseEntity;
import ru.vtb.stub.filter.RequestWrapper;

public interface ResponseService {

    ResponseEntity<Object> sendResponse(String rpsRequest, String key, RequestWrapper servletRequest);

}
