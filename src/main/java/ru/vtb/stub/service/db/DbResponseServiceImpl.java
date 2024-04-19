package ru.vtb.stub.service.db;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.db.dao.EndpointDao;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.filter.RequestWrapper;
import ru.vtb.stub.service.ResponseService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbResponseServiceImpl implements ResponseService {

    private final EndpointDao endpointDao;

    @SneakyThrows
    public ResponseEntity<Object> sendResponse(String rpsRequest, String key, RequestWrapper servletRequest) {
        var path = rpsRequest.split(":")[0];
        var method = rpsRequest.split(":")[1];
        var team = getTeam(path);
        var pathNormal = getPath(path);

        var pk = new GetDataBaseRequest();
        pk.setPath(pathNormal);
        pk.setMethod(method);
        pk.setTeam(team);
        StubData data = endpointDao.getDataByPk(pk);

        if (data == null) {
            return ResponseEntity.noContent().build();
        }

        Integer wait = data.getWait();
        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", key, wait);
            Thread.sleep(wait);
        }

        return createResponse(data);
    }

    private ResponseEntity<Object> createResponse(StubData data) {
        log.info("Request to: {} --> {}", data.getPath(), data.getResponses().toString());
        Response actualData = data.getResponses().stream().findFirst().get();
        Object actualBody = getActualBody(actualData);
        int status = actualData.getStatus();
        ResponseEntity.BodyBuilder response = ResponseEntity.status(status);
        List<Header> headers = actualData.getHeaders();
        if (!ObjectUtils.isEmpty(headers)) {
            headers.forEach(h -> response.header(h.getName(), h.getValue()));
        }
        return actualBody != null ? response.body(actualBody) : response.build();
    }

    private Object getActualBody(Response actualData) {
        JsonNode jsonBody = actualData.getBody();
        String stringBody = actualData.getStringBody();
        byte[] byteArrayBody = actualData.getBodyAsByteArray();

        // Если одновременно заполнены поля body (json) и stringBody, то приоритет у body
        if (jsonBody != null) {
            return jsonBody;
        } else if (stringBody != null) {
            return stringBody.getBytes(StandardCharsets.UTF_8);
        } else return byteArrayBody;
    }

    private String getPath(String uri) {
        var pathArr = uri.split("/");
        return "/" + Arrays.stream(pathArr).skip(2).collect(Collectors.joining("/"));
    }

    private String getTeam(String path) {
        return path.split("/")[1];
    }

}
