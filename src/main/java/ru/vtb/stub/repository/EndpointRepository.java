package ru.vtb.stub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.entity.EndpointEntity;

@Repository
public interface EndpointRepository extends JpaRepository<EndpointEntity, Long> {

    EndpointEntity findByPathAndMethod(String path, String method);
}
