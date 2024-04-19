package ru.vtb.stub.db.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.stub.db.entity.ResponseEntity;
import ru.vtb.stub.db.repository.RequestHistoryRepository;
import ru.vtb.stub.domain.StubData;

@Service
@RequiredArgsConstructor
public class RequestHistoryDao {

    private final RequestHistoryRepository requestHistoryRepository;

    @Transactional
    public ResponseEntity saveSingle(StubData data) {
        return null;
    }

}
