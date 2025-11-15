package com.ceos22.cgv_clone.common.redisson.aspect;

import com.ceos22.cgv_clone.common.redisson.annotation.ConcurrencyControl;
import com.ceos22.cgv_clone.common.redisson.exception.LockAcquisitionException;
import com.ceos22.cgv_clone.common.redisson.exception.LockInterruptedException;
import com.ceos22.cgv_clone.common.redisson.util.SpELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ConcurrencyAspect {

    private static final String LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final TransactionAspect transactionAspect;

    @Around("@annotation(com.ceos22.cgv_clone.common.redisson.annotation.ConcurrencyControl)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ConcurrencyControl annotation = method.getAnnotation(ConcurrencyControl.class);

        String lockKey = generateLockKey(signature, joinPoint.getArgs(), annotation.key());

        int transactionTimeout = calculateTransactionTimeout(annotation);

        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean acquired = tryAcquireLock(lock, annotation, lockKey, method.getName());

            if (!acquired) {
                throw new LockAcquisitionException(lockKey, annotation.waitTime());
            }

            log.info("[락 획득] method: {}, lockKey: {}",
                    method.getName(), lockKey);

            return transactionAspect.proceed(joinPoint, transactionTimeout);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockInterruptedException(lockKey, e);
        } finally {
            releaseLock(lock, lockKey, method.getName());
        }
    }

    private String generateLockKey(MethodSignature signature, Object[] args, String key) {
        String[] parameterNames = signature.getParameterNames();
        Object dynamicValue = SpELParser.getDynamicValue(parameterNames, args, key);
        return LOCK_PREFIX + dynamicValue;
    }

    private int calculateTransactionTimeout(ConcurrencyControl annotation) {
        int timeout = annotation.transactionTimeout();

        if (timeout <= 0) {
            long leaseTimeInSeconds = annotation.timeUnit().toSeconds(annotation.leaseTime());
            timeout = Math.max(1, (int) leaseTimeInSeconds - 1);
        }

        return timeout;
    }

    private boolean tryAcquireLock(RLock lock, ConcurrencyControl annotation,
                                   String lockKey, String methodName) throws InterruptedException {
        long waitTime = annotation.waitTime();
        long leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();

        log.debug("[락 획득 시도] method: {}, lockKey: {}, waitTime: {}s, leaseTime: {}s",
                methodName, lockKey, waitTime, leaseTime);

        return lock.tryLock(waitTime, leaseTime, timeUnit);
    }

    private void releaseLock(RLock lock, String lockKey, String methodName) {
        try {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[락 해제] method: {}, lockKey: {}",
                        methodName, lockKey);
            }
        } catch (IllegalMonitorStateException e) {
            log.warn("[락 이미 해제됨] method: {}, lockKey: {}",
                    methodName, lockKey);
        }
    }
}