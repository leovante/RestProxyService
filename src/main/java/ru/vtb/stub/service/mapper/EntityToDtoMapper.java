package ru.vtb.stub.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.HeaderEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(config = SpringMapperConfig.class)
public interface EntityToDtoMapper {

    @Mapping(target = "team", source = "primaryKey.team")
    @Mapping(target = "path", source = "primaryKey.path")
    @Mapping(target = "method", source = "primaryKey.method")
    @Mapping(target = "wait", source = "wait")
    @Mapping(target = "index", ignore = true)
    @Mapping(target = "response", ignore = true)
    @Mapping(target = "responses", expression = "java(mapResponsesEntityToDto(data.getResponses()))")
    StubData mapEntityToStubData(EndpointEntity data);

    default List<Response> mapResponsesEntityToDto(List<ResponseEntity> res) {
        return Optional.ofNullable(res)
                .map(it -> it.stream()
                        .map(this::mapResponseEntityToDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Mapping(target = "status", source = "status")
    @Mapping(target = "headers", expression = "java(mapHeadersEntityToDto(res.getHeaders()))")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "bodyAsByteArray", ignore = true)
    @Mapping(target = "stringBody", source = "stringBody")
    Response mapResponseEntityToDto(ResponseEntity res);

    default List<Header> mapHeadersEntityToDto(List<HeaderEntity> res) {
        return Optional.ofNullable(res)
                .map(it -> it.stream()
                        .map(this::mapHeaderEntityToDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Mapping(target = "name", source = "primaryKey.name")
    @Mapping(target = "value", source = "primaryKey.value")
    Header mapHeaderEntityToDto(HeaderEntity res);

}