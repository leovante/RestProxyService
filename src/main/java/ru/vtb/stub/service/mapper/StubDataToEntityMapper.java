package ru.vtb.stub.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;
import ru.vtb.stub.db.entity.HeaderEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = SpringMapperConfig.class, uses = {MapperUtils.class})
public interface StubDataToEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "primaryKey", expression = "java(mapStubDataToEndpointPk(data))")
    @Mapping(target = "wait", source = "wait")
    @Mapping(target = "responses", expression = "java(mapResponseDtoToEntity(data))")
    @Mapping(target = "requestHistory", ignore = true)
    @Mapping(target = "isRegex", expression = "java(MapperUtils.isRegex(data.getPath()))")
    EndpointEntity mapStubDataToEndpoint(StubData data);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "team", expression = "java(MapperUtils.buildTeam(data))")
    EndpointPathMethodTeamPk mapStubDataToEndpointPk(StubData data);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "headers", source = "headers")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "index", ignore = true)
    @Mapping(target = "currentIndex", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "endpoint", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ResponseEntity mapStubDataToEntity(Response data);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "primaryKey.name", source = "name")
    @Mapping(target = "primaryKey.value", source = "value")
    @Mapping(target = "response", ignore = true)
    HeaderEntity mapHeaderToEntity(Header header);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "team", source = "team")
    EndpointPathMethodTeamPk mapBaseRequestToEndpointPathMethodTeamPk(GetDataBaseRequest data);

    default List<ResponseEntity> mapResponseDtoToEntity(StubData data) {
        if (data.getResponses() != null && !data.getResponses().isEmpty()) {
            return data.getResponses().stream()
                    .map(this::mapStubDataToEntity)
                    .peek(it -> {
                        if (it.getBody() != null && it.getBody().equals("null")) {
                            it.setBody(null);
                        }
                    })
                    .collect(Collectors.toList());
        } else if (data.getResponse() != null) {
            return List.of(mapStubDataToEntity(data.getResponse()));
        }
        return Collections.emptyList();
    }

}
