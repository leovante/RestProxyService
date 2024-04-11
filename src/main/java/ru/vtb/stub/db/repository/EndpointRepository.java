package ru.vtb.stub.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.db.entity.EndpointEntity;
import ru.vtb.stub.db.entity.EndpointPathMethodTeamPk;

import java.util.Optional;

@Repository
public interface EndpointRepository extends JpaRepository<EndpointEntity, EndpointPathMethodTeamPk> {

    Optional<EndpointEntity> findByPrimaryKey(EndpointPathMethodTeamPk endpointPathMethodTeamPk);

}
