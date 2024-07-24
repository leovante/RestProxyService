package ru.vtb.stub.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.EndpointEntity;
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

    default List<Response> mapResponsesEntityToDto(List<Response> data) {
        return data != null && data.size() > 1 ? data : null;
    }

    default Response mapResponseEntityToDto(List<Response> data) {
        return data != null && data.size() <= 1 ? data.get(0) : null;
    }

}
