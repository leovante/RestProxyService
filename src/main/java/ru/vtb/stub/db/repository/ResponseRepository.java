package ru.vtb.stub.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;
import ru.vtb.stub.db.entity.ResponseEntity;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseEntity, Long> {

    @Query(value = "select e.* "
            + "from response e "
            + "join endpoint e2 on e2.path = e.endpoint_path and e2.method = e.endpoint_method and e2.team = e.endpoint_team "
            + "where regexp_match(e.endpoint_path, '^' || REPLACE(:#{#pk.path}, '--', '[a-zA-Z0-9.@%/_-]+') || '$') is not null and is_regex = false "
            + "or :#{#pk.path} is not null and is_regex = true "
            + "  and e.endpoint_team = :#{#pk.team} "
            + "  and e.endpoint_method = :#{#pk.method.name()} ", nativeQuery = true)
    List<ResponseEntity> findByTeamAndPathAndMethod(EndpointPathMethodTeamPk pk);

}
