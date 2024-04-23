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
            + "using endpoint e2 "
            + "where ((regexp_match(:#{#pk.path}, e.endpoint_path) is not null and is_regex = true) "
            + "    or (e.endpoint_path = :#{#pk.path} and is_regex = false)) "
            + "   or ((regexp_match(e.endpoint_path, '^' || :#{#pk.path} || '$') is not null and is_regex = false) "
            + "    or (:#{#pk.path} = e.endpoint_path and is_regex = true)) "
            + "  and e.endpoint_team = :#{#pk.team} "
            + "  and e.endpoint_method = :#{#pk.method.name()} "
            + "  and e2.path = e.endpoint_path "
            + "  and e2.method = e.endpoint_method "
            + "  and e2.team = e.endpoint_team ", nativeQuery = true)
    void removeByEndpointPk(EndpointPathMethodTeamPk pk);

}
