package ru.vtb.stub.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vtb.stub.db.entity.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseEntity, Long> {

    Optional<List<ResponseEntity>> findByTeamAndPathAndMethod(String team, String path, RequestMethod method);

}
