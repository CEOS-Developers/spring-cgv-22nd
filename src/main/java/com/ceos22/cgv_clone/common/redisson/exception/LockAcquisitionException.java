package com.ceos22.cgv_clone.common.redisson.exception;

public class LockAcquisitionException extends DistributedLockException{

    private final String lockKey;
    private final long waitTime;

    public LockAcquisitionException(String lockKey, long waitTime) {
        super(String.format("락 획득 실패 : lockKey: %s, waitTime: %s", lockKey, waitTime));

        this.lockKey = lockKey;
        this.waitTime = waitTime;
    }

    public String getLockKey() {
        return lockKey;
    }

    public long getWaitTime() {
        return waitTime;
    }
}
