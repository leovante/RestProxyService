package ru.vtb.stub.db.repository;

import io.micronaut.data.mongodb.annotation.MongoDeleteQuery;
import io.micronaut.data.mongodb.annotation.MongoFindQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
import ru.vtb.stub.db.entity.RequestHistoryEntity;

import java.util.List;

@MongoRepository
public interface RequestHistoryRepository extends CrudRepository<RequestHistoryEntity, String> {

    @MongoDeleteQuery(value = ""
            + "{ "
            + "    'endpoint': {$in: :entityList} "
            + "} ")
    void deleteByEndpoints(List<String> entityList);

    @MongoDeleteQuery(value = ""
            + "{ "
            + "    'endpoint_id.team': {$eq: :team} "
            + "} ")
    void deleteByTeam(String team);

    @MongoFindQuery(value = ""
            + "{ "
            + "    'endpoint': {$eq: :endpoint} "
            + "} ")
    List<RequestHistoryEntity> findAllByEndpointId(String endpoint);

}
