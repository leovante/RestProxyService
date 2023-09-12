package ru.vtb.stub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.entity.TeamEntity;

import javax.transaction.Transactional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    TeamEntity findByCode(String code);

    @Modifying
    @Transactional
    @Query("DELETE FROM TeamEntity t WHERE t.code=?1")
    void deleteByCode(String code);
}
