package ru.vtb.stub.db.repository;

import io.micronaut.data.mongodb.annotation.MongoDeleteQuery;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.TeamEntity;

@MongoRepository
public interface TeamRepository extends CrudRepository<TeamEntity, String> {

    @Transactional
    @MongoDeleteQuery("DELETE FROM TeamEntity t WHERE t.code=?1")
    void deleteByCode(String code);

}
