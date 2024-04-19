package ru.vtb.stub.service.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Сервис для выполнения методов с поддержкой транзакции.
 */
@Service
public class TransactionalService {

    private final PlatformTransactionManager transactionManager;

    private final TransactionTemplate transactionTemplateNewTransaction;

    private final TransactionTemplate transactionTemplateDefault;

    public TransactionalService(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.transactionTemplateDefault = getTransactionTemplate(
                TransactionDefinition.PROPAGATION_REQUIRED);
        this.transactionTemplateNewTransaction = getTransactionTemplate(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * Выполнить действие в текущей транзакции PROPAGATION_REQUIRED
     *
     * @param runnable
     *         Действие {@link Runnable}
     */
    public void execute(Runnable runnable) {
        transactionTemplateDefault.executeWithoutResult(
                transactionStatus -> runnable.run());
    }

    /**
     * Выполнить действие в новой транзакции PROPAGATION_REQUIRES_NEW
     *
     * @param runnable
     *         Действие {@link Runnable}
     */
    public void executeInNewTransaction(Runnable runnable) {
        transactionTemplateNewTransaction
                .executeWithoutResult(transactionStatus -> runnable.run());
    }

    /**
     * Выполнить действие consumer, принимающий объект consumerObject
     * в новой транзакции PROPAGATION_REQUIRES_NEW
     *
     * @param consumer
     *         Действие {@link Consumer}
     * @param consumerObject
     *         Объект типа {@link T}
     */
    public <T> void executeInNewTransaction(T consumerObject,
                                            Consumer<T> consumer) {
        transactionTemplateNewTransaction
                .executeWithoutResult(transactionStatus -> consumer.accept(consumerObject));
    }

    /**
     * Выполнить действие supplier в новой транзакции.
     *
     * @param supplier
     *         действие.
     * @param <T>
     *         тип результата.
     *
     * @return результат.
     */
    public <T> T executeInNewTransaction(Supplier<T> supplier) {
        return transactionTemplateNewTransaction.execute(status -> supplier.get());
    }

    private TransactionTemplate getTransactionTemplate(int propagationBehavior) {
        var transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        transactionTemplate.setPropagationBehavior(propagationBehavior);
        return transactionTemplate;
    }

}
