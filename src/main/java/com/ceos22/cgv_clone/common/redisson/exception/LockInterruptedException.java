package com.ceos22.cgv_clone.common.redisson.exception;

public class LockInterruptedException extends DistributedLockException {

    private final String lockKey;

    public LockInterruptedException(String lockKey, InterruptedException cause) {
        super(String.format("락 획득 대기 중 인터럽트가 발생 [lockKey: %s]", lockKey), cause);
        this.lockKey = lockKey;
    }

    public String getLockKey() {
        return lockKey;
    }
}