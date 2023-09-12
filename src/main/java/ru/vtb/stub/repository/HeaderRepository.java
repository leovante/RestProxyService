package ru.vtb.stub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.entity.HeaderEntity;

@Repository
public interface HeaderRepository extends JpaRepository<HeaderEntity, Long> {
}
