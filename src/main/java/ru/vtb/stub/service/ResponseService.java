package ru.vtb.stub.service;

import org.springframework.http.ResponseEntity;
import ru.vtb.stub.filter.RequestWrapper;

import java.util.List;

public interface ResponseService {

    ResponseEntity<Object> sendResponse(String rpsRequest, String key, RequestWrapper servletRequest);

}
