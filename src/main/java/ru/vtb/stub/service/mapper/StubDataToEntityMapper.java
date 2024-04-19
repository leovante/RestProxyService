package ru.vtb.stub.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;
import ru.vtb.stub.db.entity.HeaderEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;

@Mapper(config = SpringMapperConfig.class)
public interface StubDataToEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "response.status")
    @Mapping(target = "headers", source = "response.headers")
    @Mapping(target = "body", source = "response.body")
    @Mapping(target = "stringBody", source = "response.stringBody")
    @Mapping(target = "index", ignore = true)
    @Mapping(target = "currentIndex", ignore = true)
    @Mapping(target = "endpoint", expression = "java(mapStubDataToEndpoint(data))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ResponseEntity mapStubDataToEntity(StubData data);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "primaryKey.name", source = "name")
    @Mapping(target = "primaryKey.value", source = "value")
    @Mapping(target = "response", ignore = true)
    HeaderEntity mapHeaderToEntity(Header header);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "primaryKey", expression = "java(mapStubDataToEndpointPk(data))")
    @Mapping(target = "wait", source = "wait")
    @Mapping(target = "responses", ignore = true)
    EndpointEntity mapStubDataToEndpoint(StubData data);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "team", source = "team")
    EndpointPathMethodTeamPk mapStubDataToEndpointPk(StubData data);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "team", source = "team")
    EndpointPathMethodTeamPk mapBaseRequestToEndpointPathMethodTeamPk(GetDataBaseRequest key);
}
