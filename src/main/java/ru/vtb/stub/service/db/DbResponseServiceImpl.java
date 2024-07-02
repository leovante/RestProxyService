package ru.vtb.stub.service.db;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.convert.DefaultMutableConversionService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpHeaders;
import io.micronaut.http.simple.SimpleHttpHeaders;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.service.ResponseService;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Requires(property = "rest-proxy-stub.storage-mode", value = "db")
@Singleton
public class DbResponseServiceImpl implements ResponseService {

    private final RequestService requestService;

    @SneakyThrows
    public HttpResponse<?> sendResponse(String rpsRequest, String team, HttpRequest servletRequest) {
        var path = rpsRequest.split(":")[0];
        var method = rpsRequest.split(":")[1];
        var pathNormal = getPath(path, team);

        var pk = new GetDataBaseRequest();
        pk.setPath(pathNormal);
        pk.setMethod(method);
        pk.setTeam(team);
        StubData data = requestService.getDataByPkAndMarkUsed(pk);

        if (data == null) {
            return HttpResponse.noContent();
        }

        Integer wait = data.getWait();
        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", path, wait);
            Thread.sleep(wait);
        }

        return createResponse(data);
    }

    private HttpResponse<Object> createResponse(StubData data) {
        log.info("Request to path: {}", data.getPath());
        Response actualData = Optional.ofNullable(data.getResponses())
                .map(it -> it.stream().filter(Response::getIsUsed).reduce((x, y) -> y).get())
                .orElse(data.getResponse());

        Object actualBody = getActualBody(actualData);
        int status = actualData.getStatus();

        Consumer<MutableHttpHeaders> headers = it -> new SimpleHttpHeaders(
                actualData.getHeaders().stream().collect(Collectors.toMap(Header::getName, Header::getValue)),
                new DefaultMutableConversionService());

        return Optional.ofNullable(actualBody)
                .map(it -> HttpResponse.status(HttpStatus.valueOf(status)).headers(headers).body(it))
                .orElse(HttpResponse.status(HttpStatus.valueOf(status)));
    }

    private Object getActualBody(Response actualData) {
        Object jsonBody = actualData.getBody();
        byte[] byteArrayBody = actualData.getBodyAsByteArray();

        // Если одновременно заполнены поля body (json) и stringBody, то приоритет у body
        if (jsonBody != null) {
            return jsonBody;
        } else return byteArrayBody;
    }

    private String getPath(String path, String team) {
        return path.replace("/" + team + "/", "/");
    }

}
