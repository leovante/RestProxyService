package ru.vtb.stub.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import ru.vtb.stub.filter.RequestWrapper;
import ru.vtb.stub.service.RequestHistoryService;
import ru.vtb.stub.service.ResponseService;
import ru.vtb.stub.service.transaction.HistoryBypassService;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ResponseController {

    private final ResponseService responseService;
    private final RequestHistoryService requestHistoryService;
    private final HistoryBypassService historyBypassService;

    @Operation(hidden = true)
    @RequestMapping(path = "${path.response}", method = {GET, POST, PUT, PATCH, DELETE},
            produces = {APPLICATION_JSON_VALUE, TEXT_PLAIN_VALUE, "application/vnd.schemaregistry.v1+json",
                    APPLICATION_OCTET_STREAM_VALUE, MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> response(
            @RequestParam String rpsRequest,
            @RequestParam String rpsKey,
            RequestWrapper servletRequest
    ) {
        return historyBypassService.executeWithHistory(
                () -> requestHistoryService.saveRequest(rpsRequest, rpsKey, servletRequest),
                () -> responseService.sendResponse(rpsRequest, rpsKey, servletRequest)
        );
    }

    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e,
                                                                            HttpServletRequest request) {
        log.error("No acceptable representation found for [{}] | supported {}", request.getHeader("Accept"),
                e.getSupportedMediaTypes());
        return ResponseEntity.badRequest().build();
    }

}
