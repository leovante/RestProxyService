package ru.vtb.stub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.entity.EndpointEntity;
import ru.vtb.stub.entity.ResponseEntity;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseEntity, Long> {

    List<ResponseEntity> findByEndpoint(EndpointEntity endpointEntity);
}
