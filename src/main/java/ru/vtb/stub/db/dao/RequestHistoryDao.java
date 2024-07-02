package ru.vtb.stub.db.dao;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpParameters;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.body.ByteBody;
import io.micronaut.http.server.netty.NettyHttpRequest;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.vtb.stub.db.entity.RequestHistoryEntity;
import ru.vtb.stub.db.repository.RequestHistoryRepository;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.dto.GetDataBaseRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Singleton
public class RequestHistoryDao {

    private final EndpointDao endpointDao;

    private final RequestHistoryRepository requestHistoryRepository;

    @Transactional
    public List<Request> getHistoryRequestByPk(GetDataBaseRequest key) {
        return endpointDao.getEndpointByPk(key)
                .map(it -> requestHistoryRepository.findAllByEndpointId(it.getId()))
                .map(it -> it.stream().map(RequestHistoryEntity::getRequest).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Transactional
    public void save(String path, String method, String team, HttpRequest<?> httpRequest) {
        var pathNormal = getPathWithoutTeam(path);
        var endpoint = endpointDao.getEndpointByPk(team, pathNormal, method);

        if (endpoint.isEmpty()) {
            return;
        }
        var endpointId = endpoint.get().getId();

        Request request = Request.builder()
                .date(LocalDateTime.now())
                .path(path)
                .method(method)
                .headers(getHeaders(httpRequest.getHeaders()))
                .params(getQueryParams(httpRequest.getParameters()))
                .body(getBody(httpRequest))
                .build();

        var historySnapshot = new RequestHistoryEntity();
        historySnapshot.setRequest(request);
        historySnapshot.setEndpoint(endpointId);
        historySnapshot.setEndpointId(endpoint.get().getSecondId());
        requestHistoryRepository.save(historySnapshot);
    }

    private Map<String, String> getHeaders(@NonNull HttpHeaders headers) {
        return headers.names().stream()
                .collect(Collectors.toMap(
                        name -> name,
                        headers::get));
    }

    private Map<String, List<String>> getQueryParams(HttpParameters parameters) {
        return parameters.names().stream()
                .collect(Collectors.toMap(
                        name -> name,
                        parameters::getAll));
    }

    @SneakyThrows
    private String getBody(HttpRequest<?> httpRequest) {
        ByteBody stream = ((NettyHttpRequest) httpRequest).byteBody();
        return new String(stream.toInputStream().readAllBytes());
    }

    private String getPathWithoutTeam(String uri) {
        String[] pathArr = uri.split("/");
        return uri.substring(pathArr[1].length() + 1);
    }

}
