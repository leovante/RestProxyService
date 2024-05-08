package ru.vtb.stub.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;

import java.util.List;
import java.util.Optional;

@Repository
public interface EndpointRepository extends JpaRepository<EndpointEntity, EndpointPathMethodTeamPk> {

    @Query(value = "select e.* "
            + "from endpoint e "
            + "where regexp_match(e.path, '^' || REPLACE(:#{#pk.path}, '--', '[a-zA-Z0-9.@%/_-]+') || '$') is not null and is_regex = false "
            + "or :#{#pk.path} is not null and is_regex = true "
            + "  and e.team = :#{#pk.team} "
            + "  and e.method = :#{#pk.method.name()} ", nativeQuery = true)
    Optional<EndpointEntity> findByPrimaryKey(EndpointPathMethodTeamPk pk);

    @Query("select endpoint from EndpointEntity endpoint where endpoint.primaryKey.team = :team")
    Optional<List<EndpointEntity>> findTop30ByTeam(@Param("team") String team);

    @Query(value = "select e.* from endpoint e where team = :team limit 1", nativeQuery = true)
    Optional<EndpointEntity> findTop1ByTeam(@Param("team") String team);

    @Modifying
    @Query(value = "delete from endpoint e "
            + "where regexp_match(e.path, '^' || REPLACE(:#{#pk.path}, '--', '[a-zA-Z0-9.@%/_-]+') || '$') is not null and is_regex = false "
            + "or :#{#pk.path} is not null and is_regex = true "
            + "  and e.team = :#{#pk.team} "
            + "  and e.method = :#{#pk.method.name()} ", nativeQuery = true)
    void removeByPrimaryKey(EndpointPathMethodTeamPk pk);

    @Modifying
    @Query("delete from EndpointEntity endpoint where endpoint.primaryKey.team = :team")
    void removeByTeam(String team);

}
