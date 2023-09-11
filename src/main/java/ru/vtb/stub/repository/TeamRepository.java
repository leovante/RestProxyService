package ru.vtb.stub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vtb.stub.entity.Team;

import javax.transaction.Transactional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Modifying
    @Query("delete from Team t where t.code=?1")
    @Transactional
    void deleteByCode(String code);
}
