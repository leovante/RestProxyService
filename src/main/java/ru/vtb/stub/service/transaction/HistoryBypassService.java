package ru.vtb.stub.service.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class HistoryBypassService {

    @Value("${rest-proxy-stub.is-save-history:true}")
    private Boolean isSaveHistory;

    private final TransactionalService transactionalService;

    public ResponseEntity<Object> executeWithHistory(
            Runnable history,
            Supplier<ResponseEntity<Object>> action
    ) {
        if (isSaveHistory) {
            transactionalService.executeInNewTransaction(history);
        }
        return transactionalService.executeInNewTransaction(action);
    }

}
