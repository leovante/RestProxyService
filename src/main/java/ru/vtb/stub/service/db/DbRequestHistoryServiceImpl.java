package ru.vtb.stub.service.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.db.dao.EndpointDao;
import ru.vtb.stub.db.entity.RequestHistoryEntity;
import ru.vtb.stub.db.repository.RequestHistoryRepository;
import ru.vtb.stub.domain.Request;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.filter.RequestWrapper;
import ru.vtb.stub.service.RequestHistoryService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class DbRequestHistoryServiceImpl implements RequestHistoryService {

    private final RequestHistoryRepository requestHistoryRepository;
    private final EndpointDao endpointDao;

    @Transactional
    public void saveRequest(String rpsRequest, String rpsKey, RequestWrapper servletRequest) {
        var path = rpsRequest.split(":")[0];
        var method = rpsRequest.split(":")[1];
        var team = getTeam(path);
        var pathNormal = getPath(path);

        var endpoint = endpointDao.getDataByPk(new GetDataBaseRequest(team, pathNormal, method));
        if (endpoint.isEmpty()) {
            return;
        }

        Request request = Request.builder()
                .date(LocalDateTime.now())
                .path(rpsRequest.split(":")[0])
                .method(rpsRequest.split(":")[1])
                .headers(getHeaders(servletRequest))
                .params(getQueryParams(servletRequest.getQueryString()))
                .body(servletRequest.getReader()
                        .lines()
                        .collect(Collectors.joining(System.lineSeparator())))
                .build();

        var snapshot = new RequestHistoryEntity();
        snapshot.setRequest(request);
        snapshot.setEndpoint(endpoint.get());
        requestHistoryRepository.save(snapshot);
    }

    private Map<String, String> getHeaders(RequestWrapper request) {
        Enumeration<String> headers = request.getHeaderNames();
        return ObjectUtils.isEmpty(headers)
                ? null
                : Collections.list(headers)
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
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

    private String getPath(String uri) {
        var pathArr = uri.split("/");
        return "/" + Arrays.stream(pathArr).skip(2).collect(Collectors.joining("/"));
    }

    private String getTeam(String path) {
        return path.split("/")[1];
    }

}
