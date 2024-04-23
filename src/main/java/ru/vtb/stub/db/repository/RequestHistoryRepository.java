package ru.vtb.stub.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;
import ru.vtb.stub.db.entity.RequestHistoryEntity;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistoryEntity, Long> {

    @Modifying
    @Query(value = "delete from request_history e "
            + "where (regexp_match(:#{#pk.path}, e.endpoint_path) is not null  "
            + "  or regexp_match(e.endpoint_path, :#{#pk.path}) is not null) "
            + "  and e.endpoint_team = :#{#pk.team} "
            + "  and e.endpoint_method = :#{#pk.method.name()} ", nativeQuery = true)
    void removeByEndpointPk(EndpointPathMethodTeamPk pk);

}
