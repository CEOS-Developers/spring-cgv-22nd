package com.ceos22.cgv_clone.common.redisson.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
@Slf4j
public class TransactionAspect {

    private final PlatformTransactionManager transactionManager;

    public TransactionAspect(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object proceed(ProceedingJoinPoint joinPoint, int timeout) throws Throwable {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        definition.setTimeout(timeout);  // 동적 타임아웃 설정

        TransactionStatus status = transactionManager.getTransaction(definition);

        log.debug("[트랜잭션 시작] method: {}, timeout: {}초",
                joinPoint.getSignature().getName(), timeout);

        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            log.debug("[트랜잭션 커밋] method: {}", joinPoint.getSignature().getName());
            return result;

        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("[트랜잭션 롤백] method: {}, error: {}",
                    joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
    }
}



