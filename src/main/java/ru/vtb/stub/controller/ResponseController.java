package ru.vtb.stub.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import lombok.RequiredArgsConstructor;
import ru.vtb.stub.service.RequestHistoryService;
import ru.vtb.stub.service.ResponseService;
import ru.vtb.stub.service.transaction.HistoryBypassService;

@ExecuteOn(TaskExecutors.BLOCKING)
@RequiredArgsConstructor
@Controller("${path.response}")
public class ResponseController {

    private final ResponseService responseService;
    private final RequestHistoryService requestHistoryService;
    private final HistoryBypassService historyBypassService;

    @Post
    @Consumes(MediaType.ALL)
    public HttpResponse<?> responsePost(HttpRequest<?> servletRequest) {
        String rpsRequest = servletRequest.getParameters().get("rpsRequest");
        String rpsKey = servletRequest.getParameters().get("rpsKey");
        return historyBypassService.executeWithHistory(
                () -> requestHistoryService.saveRequest(rpsRequest, rpsKey, servletRequest),
                () -> responseService.sendResponse(rpsRequest, rpsKey, servletRequest)
        );
    }

    @Get
    @Consumes(MediaType.ALL)
    public HttpResponse<?> responseGet(HttpRequest<?> servletRequest) {
        String rpsRequest = servletRequest.getParameters().get("rpsRequest");
        String rpsKey = servletRequest.getParameters().get("rpsKey");
        return historyBypassService.executeWithHistory(
                () -> requestHistoryService.saveRequest(rpsRequest, rpsKey, servletRequest),
                () -> responseService.sendResponse(rpsRequest, rpsKey, servletRequest)
        );
    }

    @Put
    @Consumes(MediaType.ALL)
    public HttpResponse<?> responsePut(HttpRequest<?> servletRequest) {
        String rpsRequest = servletRequest.getParameters().get("rpsRequest");
        String rpsKey = servletRequest.getParameters().get("rpsKey");
        return historyBypassService.executeWithHistory(
                () -> requestHistoryService.saveRequest(rpsRequest, rpsKey, servletRequest),
                () -> responseService.sendResponse(rpsRequest, rpsKey, servletRequest)
        );
    }

    @Delete
    @Consumes(MediaType.ALL)
    public HttpResponse<?> responseDelete(HttpRequest<?> servletRequest) {
        String rpsRequest = servletRequest.getParameters().get("rpsRequest");
        String rpsKey = servletRequest.getParameters().get("rpsKey");
        return historyBypassService.executeWithHistory(
                () -> requestHistoryService.saveRequest(rpsRequest, rpsKey, servletRequest),
                () -> responseService.sendResponse(rpsRequest, rpsKey, servletRequest)
        );
    }

}
