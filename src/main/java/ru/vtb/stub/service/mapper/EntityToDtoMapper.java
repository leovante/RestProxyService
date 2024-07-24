package ru.vtb.stub.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;

import java.util.List;

@Mapper(config = SpringMapperConfig.class)
public interface EntityToDtoMapper {

    @Mapping(target = "team", source = "primaryKey.team")
    @Mapping(target = "path", source = "primaryKey.path")
    @Mapping(target = "method", source = "primaryKey.method")
    @Mapping(target = "wait", source = "wait")
    @Mapping(target = "response", expression = "java(mapResponseEntityToDto(data.getResponses()))")
    @Mapping(target = "responses", expression = "java(mapResponsesEntityToDto(data.getResponses()))")
    StubData mapEntityToStubData(EndpointEntity data);

    default List<Response> mapResponsesEntityToDto(List<ResponseEntity> data) {
        return data != null && data.size() > 1 ? data.stream().map(this::mapResponseEntityToDto).toList() : null;
    }

    default Response mapResponseEntityToDto(List<ResponseEntity> data) {
        return data != null && data.size() <= 1 ? mapResponseEntityToDto(data.get(0)) : null;
    }

    @Mapping(target = "status", source = "status")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "headers", source = "headers")
    @Mapping(target = "bodyAsByteArray", source = "bodyAsByteArray")
    @Mapping(target = "template", source = "template")
    @Mapping(target = "idx", source = "idx")
    Response mapResponseEntityToDto(ResponseEntity response);

}
