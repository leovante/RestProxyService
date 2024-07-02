package ru.vtb.stub.service.transaction;

import io.micronaut.transaction.TransactionDefinition;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Singleton;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Сервис для выполнения методов с поддержкой транзакции.
 */
@Singleton
public class TransactionalService {

    private final TransactionOperations<Object> transactionOperations;

    public TransactionalService(TransactionOperations<Object> transactionOperations) {
        this.transactionOperations = transactionOperations;
    }

    /**
     * Выполнить действие в текущей транзакции PROPAGATION_REQUIRED
     *
     * @param runnable Действие {@link Runnable}
     */
    public void execute(Runnable runnable) {
        /*transactionTemplateDefault.executeWithoutResult(
                transactionStatus -> runnable.run());*/
    }

    /**
     * Выполнить действие в новой транзакции PROPAGATION_REQUIRES_NEW
     *
     * @param runnable Действие {@link Runnable}
     */
    public void executeInNewTransaction(Runnable runnable) {
        /*Thread.startVirtualThread(
                () ->*/
        transactionOperations.execute(
                TransactionDefinition.of(TransactionDefinition.Propagation.REQUIRES_NEW),
                status -> {
                    runnable.run();
                    return "SUCCESS";
                })
//        )
        ;
    }

    /**
     * Выполнить действие consumer, принимающий объект consumerObject
     * в новой транзакции PROPAGATION_REQUIRES_NEW
     *
     * @param consumer       Действие {@link Consumer}
     * @param consumerObject Объект типа {@link T}
     */
    public <T> void executeInNewTransaction(T consumerObject,
                                            Consumer<T> consumer) {
        /*transactionTemplateNewTransaction
                .executeWithoutResult(transactionStatus -> consumer.accept(consumerObject));*/
    }

    /**
     * Выполнить действие supplier в новой транзакции.
     *
     * @param supplier действие.
     * @param <T>      тип результата.
     * @return результат.
     */
    public <T> T executeInNewTransaction(Supplier<T> supplier) {
        return transactionOperations.execute(
                TransactionDefinition.of(TransactionDefinition.Propagation.REQUIRES_NEW),
                status -> supplier.get()
        );
    }

}
