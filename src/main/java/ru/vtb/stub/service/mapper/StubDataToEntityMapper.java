package ru.vtb.stub.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;
import ru.vtb.stub.domain.Response;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;

import java.util.Collections;
import java.util.List;

@Mapper(config = SpringMapperConfig.class, uses = {MapperUtils.class})
public interface StubDataToEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "primaryKey", expression = "java(mapStubDataToEndpointPk(data))")
    @Mapping(target = "wait", source = "wait")
    @Mapping(target = "responses", expression = "java(mapResponseDtoToEntity(data))")
    @Mapping(target = "requestHistory", ignore = true)
    @Mapping(target = "idx", expression = "java(new Integer(0))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isRegex", expression = "java(MapperUtils.isRegex(data.getPath()))")
    EndpointEntity mapStubDataToEndpoint(StubData data);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "team", expression = "java(MapperUtils.buildTeam(data))")
    EndpointPathMethodTeamPk mapStubDataToEndpointPk(StubData data);

    @Mapping(target = "path", source = "path")
    @Mapping(target = "method", source = "method")
    @Mapping(target = "team", source = "team")
    EndpointPathMethodTeamPk mapBaseRequestToEndpointPathMethodTeamPk(GetDataBaseRequest data);

    default List<Response> mapResponseDtoToEntity(StubData data) {
        if (data.getResponses() != null && !data.getResponses().isEmpty()) {

            return data.getResponses();
        } else if (data.getResponse() != null) {
            return List.of(data.getResponse());
        }
        return Collections.emptyList();
    }

}
