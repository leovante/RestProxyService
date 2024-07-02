package ru.vtb.stub.service.local;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.convert.DefaultMutableConversionService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpHeaders;
import io.micronaut.http.simple.SimpleHttpHeaders;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.service.ResponseService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ru.vtb.stub.data.DataMap.*;

@Slf4j
@Requires(property = "rest-proxy-stub.storage-mode", value = "ram")
@Singleton
public class ResponseServiceImpl implements ResponseService {

    @SneakyThrows
    public HttpResponse<Object> sendResponse(String rpsRequest, String key, HttpRequest servletRequest) {
        StubData data = key.endsWith("$") ? dataByRegexMap.get(key) : dataByKeyMap.get(key);

        Request request = Request.builder()
                .date(LocalDateTime.now())
                .path(rpsRequest.split(":")[0])
                .method(rpsRequest.split(":")[1])
                .headers(getHeaders(servletRequest))
//                .params(getQueryParams(servletRequest.getQueryString()))
//                .body(servletRequest.getReader()
//                        .lines()
//                        .collect(Collectors.joining(System.lineSeparator())))
                .build();

        Integer wait = data.getWait();
        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", key, wait);
            Thread.sleep(wait);
        }

        Response actualData = getActualData(data);
        Object actualBody = getActualBody(actualData);
        int status = actualData.getStatus();

        List<Request> history = requestMap.computeIfAbsent(key, k -> new ArrayList<>());

        if (status >= HttpStatus.BAD_REQUEST.getCode()) {
            if (actualBody != null) {
                log.info("Request to: {} --> Response with error: {}, body: {}", key, status, actualBody);
                updateHistory(history, request, key);
                return HttpResponse.status(HttpStatus.valueOf(status)).body(actualBody);
            } else {
                log.info("Request to: {} --> Response with error: {}", key, status);
                updateHistory(history, request, key);
                return HttpResponse.status(status, "Test error message");
            }
        }

        Consumer<MutableHttpHeaders> headers = it -> new SimpleHttpHeaders(
                actualData.getHeaders().stream().collect(Collectors.toMap(Header::getName, Header::getValue)),
                new DefaultMutableConversionService());

        log.info("Request to: {} --> {}", key, actualData);
        updateHistory(history, request, key);
        return Optional.ofNullable(actualBody)
                .map(it -> HttpResponse.status(HttpStatus.valueOf(status)).headers(headers).body(it))
                .orElse(HttpResponse.status(HttpStatus.valueOf(status)));
    }

    private Map<String, String> getHeaders(HttpRequest request) {
//        Enumeration<String> headers = request.getHeaderNames();
        return /*CollectionUtils.isEmpty(headers)
                ? null
                : Collections.list(headers)
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));*/ null;
    }

    private Map<String, List<String>> getQueryParams(String queryString) {
        String[] params = queryString.split("&");
        return params.length == 0
                ? null
                : Arrays.stream(params)
                .map(p -> p.split("="))
                .skip(2)
                .collect(Collectors.groupingBy(
                        p -> p[0],
                        Collectors.mapping(p -> p.length > 1 ? p[1] : "", Collectors.toList())
                ));
    }

    private Response getActualData(StubData data) {
        return Optional.of(data.getResponses())
                .map(it -> it.stream()
                        .filter(it2 -> !it2.getIsUsed())
                        .peek(it3 -> it3.setIsUsed(true))
                        .findFirst().get())
                .orElse(data.getResponse());
    }

    private Object getActualBody(Response actualData) {
        Object jsonBody = actualData.getBody();
        byte[] byteArrayBody = actualData.getBodyAsByteArray();

        // Если одновременно заполнены поля body (json) и stringBody, то приоритет у body
        if (jsonBody != null && !jsonBody.equals("null")) {
            return jsonBody;
        } else return byteArrayBody;
    }

    private void updateHistory(List<Request> history, Request request, String key) {
        history.add(request);
        log.debug("Updated history: {} --> {}", key, requestMap.get(key));
    }

}
