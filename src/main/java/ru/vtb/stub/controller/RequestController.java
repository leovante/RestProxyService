package ru.vtb.stub.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.validate.Team;

import java.util.List;

@Validated
@RequiredArgsConstructor
@Controller("${path.data}")
public class RequestController {

    private final RequestService service;

    @Post
    @Status(HttpStatus.CREATED)
    public void putData(@Valid @Body StubData data) {
        service.putData(data);
    }

    @Get
    public HttpResponse<List<StubData>> getData(@Valid @RequestBean GetDataBaseRequest getDataBaseRequest) {
        return HttpResponse.ok(service.getData(getDataBaseRequest));
    }

    @Get("/{team}")
    public HttpResponse<StubData[]> getTeamData(@PathVariable @Team String team) {
        return HttpResponse.ok(service.getTeamData(team));
    }

    @Delete
    @Status(HttpStatus.OK)
    public void removeData(@Valid @RequestBean GetDataBaseRequest getDataBaseRequest) {
        service.removeData(getDataBaseRequest);
    }

    @Delete("/{team}")
    @Status(HttpStatus.OK)
    public void removeTeamData(@PathVariable @Team String team) {
        service.removeTeamData(team);
    }

    @Get("/history")
    public HttpResponse<List<Request>> getHistory(@Valid @RequestBean GetDataBaseRequest getDataBaseRequest) {
        return HttpResponse.ok(service.getHistory(getDataBaseRequest));
    }
}
