package ru.vtb.stub.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.db.entity.HeaderEntity;

@Repository
public interface HeaderRepository extends JpaRepository<HeaderEntity, Long> {
}
