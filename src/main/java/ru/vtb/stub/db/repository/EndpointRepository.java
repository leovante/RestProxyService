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

    Optional<EndpointEntity> findByPrimaryKey(EndpointPathMethodTeamPk endpointPathMethodTeamPk);

    @Query(value = "select e.* "
            + "from endpoint e "
            + "where regexp_match(:path, e.path) is not null "
            + "  and team = :team "
            + "  and method = :#{#method.name()} "
            + "  and is_regex = true", nativeQuery = true)
    Optional<EndpointEntity> findRegexByTeamAndPathAndMethod(@Param("team") String team,
                                                             @Param("path") String path,
                                                             @Param("method") RequestMethod method);

    @Query("select endpoint from EndpointEntity endpoint where endpoint.primaryKey.team = :team")
    Optional<List<EndpointEntity>> findTop30ByTeam(@Param("team") String team);

    @Query(value = "select e.* from endpoint e where team = :team limit 1", nativeQuery = true)
    Optional<EndpointEntity> findTop1ByTeam(@Param("team") String team);

    @Modifying
    void removeAllByPrimaryKey(EndpointPathMethodTeamPk endpointPathMethodTeamPk);

    @Modifying
    @Query("delete from EndpointEntity endpoint where endpoint.primaryKey.team = :team")
    void removeByTeam(String team);

}
