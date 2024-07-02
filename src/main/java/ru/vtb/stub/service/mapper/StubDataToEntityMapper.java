package ru.vtb.stub.service.mapper;

import jakarta.inject.Singleton;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;
import ru.vtb.stub.db.entity.HeaderEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jsr330", config = MicronautMapperConfig.class, uses = {MapperUtils.class})
public interface StubDataToEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "secondId", expression = "java(mapStubDataToEndpointPk(data))")
    @Mapping(target = "wait", source = "wait")
    @Mapping(target = "isRegex", expression = "java(MapperUtils.isRegex(data.getPath()))")
    @Mapping(target = "responses", expression = "java(mapResponseDtoToEntity(data))")
    EndpointEntity mapStubDataToEndpoint(StubData data);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "team", expression = "java(MapperUtils.buildTeam(data))")
    EndpointPathMethodTeamPk mapStubDataToEndpointPk(StubData data);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "headers", source = "headers")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "isUsed", expression = "java(setIsUsed(data.getIsUsed()))")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    ResponseEntity mapStubDataToEntity(Response data);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", source = "value")
    HeaderEntity mapHeaderToEntity(Header header);

    default List<ResponseEntity> mapResponseDtoToEntity(StubData data) {
        if (data.getResponses() != null && !data.getResponses().isEmpty()) {
            return data.getResponses().stream()
                    .map(this::mapStubDataToEntity)
                    .collect(Collectors.toList());
        } else if (data.getResponse() != null) {
            return List.of(mapStubDataToEntity(data.getResponse()));
        }
        return Collections.emptyList();
    }

    default Boolean setIsUsed(Boolean isUsed) {
        return isUsed != null && isUsed;
    }

}
