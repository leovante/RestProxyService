package ru.vtb.stub.service.db;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;
import ru.vtb.stub.filter.RequestWrapper;
import ru.vtb.stub.service.RequestService;
import ru.vtb.stub.service.ResponseService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbResponseServiceImpl implements ResponseService {

    private final RequestService requestService;

    @SneakyThrows
    public ResponseEntity<Object> sendResponse(String rpsRequest, String team, RequestWrapper servletRequest) {
        var path = rpsRequest.split(":")[0];
        var method = rpsRequest.split(":")[1];
        var pathNormal = getPath(path, team);

        var pk = new GetDataBaseRequest();
        pk.setPath(pathNormal);
        pk.setMethod(method);
        pk.setTeam(team);
        StubData data = requestService.getData(pk);

        if (data == null) {
            return ResponseEntity.noContent().build();
        }

        Integer wait = data.getWait();
        if (wait != null) {
            log.info("Request to: {} --> Waiting {} ms...", path, wait);
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
        String jsonBody = actualData.getBody();
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
