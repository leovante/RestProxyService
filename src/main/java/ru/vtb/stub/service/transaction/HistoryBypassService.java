package ru.vtb.stub.service.transaction;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Singleton
public class HistoryBypassService {

    @Value("${rest-proxy-stub.is-save-history:true}")
    private Boolean isSaveHistory;

    private final TransactionalService transactionalService;

    public HttpResponse<?> executeWithHistory(
            Runnable history,
            Supplier<HttpResponse<?>> action
    ) {
        if (isSaveHistory) {
            transactionalService.executeInNewTransaction(history);
        }
        return transactionalService.executeInNewTransaction(action);
    }

}
