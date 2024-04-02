package ru.vtb.stub.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vtb.stub.db.entity.HeaderEntity;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.domain.Header;
import ru.vtb.stub.domain.Response;

@Mapper(config = SpringMapperConfig.class)
public interface StubDataToEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "headers", source = "headers")
    @Mapping(target = "body", source = "body")
    @Mapping(target = "stringBody", source = "stringBody")
    @Mapping(target = "index", ignore = true)
    @Mapping(target = "currentIndex", ignore = true)
    @Mapping(target = "endpoint", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "expiredAt", ignore = true)
    ResponseEntity mapResponseDtoToEntity(Response notificationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "response", ignore = true)
    HeaderEntity mapHeaderDtoToEntity(Header header);

}
