package ru.vtb.stub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.entity.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
}
