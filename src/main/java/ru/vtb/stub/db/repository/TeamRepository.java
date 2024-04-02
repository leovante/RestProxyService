package ru.vtb.stub.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.vtb.stub.db.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    TeamEntity findByCode(String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM TeamEntity t WHERE t.code=?1")
    void deleteByCode(String code);

}
