package com.ceos22.cgv_clone.global.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, Object value, long ttlMillis) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            values.set(key, value, Duration.ofMillis(ttlMillis));
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public void setValue(String key, Object value) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            values.set(key, value);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public Object getValue(String key) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            if (values.get(key) == null) {
                return "";
            }
            return values.get(key).toString();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public void deleteValue(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public boolean checkExistsValue(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    // 키를 1씩 증가시키고, 만약 키가 처음 생성된 상태라면 만료시간(초 단위)를 설정
    public long incrementWithExpire(String key, long expireSeconds) {
        // opsForValue 로 증가
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Long value = ops.increment(key, 1L);
        if (value == null) {
            throw new IllegalStateException("Redis increment returned null for key: " + key);
        }

        // 만약 값이 1 이면 즉, 키가 새로 생성된 상태일 가능성 → 만료시간 설정
        if (value == 1L && expireSeconds > 0) {
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }
        return value;
    }

    //키 만료시간 설정
    public void expireKey(String key, long expireSeconds) {
        if (expireSeconds > 0) {
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS);
        }
    }
}
