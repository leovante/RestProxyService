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
            + "where (regexp_match(:#{#pk.path}, e.endpoint_path) is not null "
            + "  or regexp_match(e.endpoint_path, :#{#pk.path}) is not null) "
            + "  and e.endpoint_team = :#{#pk.team} "
            + "  and e.endpoint_method = :#{#pk.method.name()} ", nativeQuery = true)
    List<ResponseEntity> findByTeamAndPathAndMethod(EndpointPathMethodTeamPk pk);

}
