package ru.vtb.stub.service;

import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.filter.RequestWrapper;

import java.util.List;

public interface ResponseService {

    List<ResponseEntity> sendResponse(String rpsRequest, String key, RequestWrapper servletRequest);

}
