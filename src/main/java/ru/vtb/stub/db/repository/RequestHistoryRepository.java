package ru.vtb.stub.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.db.entity.RequestHistoryEntity;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistoryEntity, Long> {

}
