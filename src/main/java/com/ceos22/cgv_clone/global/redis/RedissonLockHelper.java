package com.ceos22.cgv_clone.global.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * - 단일 키: withLock
 * - 다중 키: withLocks(deadlock 방지하고자 키 순차 정렬)
 */
@Component
@RequiredArgsConstructor
public class RedissonLockHelper {

    private final RedissonClient redissonClient;

    // 단일 키 lock
    public <T> T withLock(String key, long waitMs, long leaseMs, Supplier<T> body) {
        RLock lock = redissonClient.getLock(key);
        boolean ok = false;
        try {
            ok = lock.tryLock(waitMs, leaseMs, TimeUnit.MILLISECONDS);
            if (!ok) {
                throw new IllegalStateException("Lock acquire failed: " + key);
            }
            return body.get();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Lock interrupted: " + key, e);
        }
        // 해제
        finally {
            if (ok && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    // 다중 키 lock
    public <T> T withLocks(List<String> keys, long waitMs, long leaseMs, Supplier<T> body) {
        List<RLock> acquired = new ArrayList<>();
        // deadlock 방지: 키를 정렬된 순서로 잠그고, 역순으로 해제
        List<String> sorted = keys.stream().distinct().sorted().toList();
        try {
            for (String key : sorted) {
                RLock lock = redissonClient.getLock(key);
                boolean ok = lock.tryLock(waitMs, leaseMs, TimeUnit.MILLISECONDS);
                if (!ok) {
                    throw new IllegalStateException("Lock acquire failed: " + key);
                }
                acquired.add(lock);
            }
            return body.get();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Lock interrupted", e);
        }
        // 해제
        finally {
            for (int i = acquired.size() - 1; i >= 0; i--) {
                RLock lock = acquired.get(i);
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
    }
}
