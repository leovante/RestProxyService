package ru.vtb.stub.service.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class HistoryBypassService {

    private final TransactionalService transactionalService;

    public ResponseEntity<Object> executeWithHistory(
            Runnable history,
            Supplier<ResponseEntity<Object>> action
    ) {
        transactionalService.executeInNewTransaction(history);
        return transactionalService.executeInNewTransaction(action);
    }

}
