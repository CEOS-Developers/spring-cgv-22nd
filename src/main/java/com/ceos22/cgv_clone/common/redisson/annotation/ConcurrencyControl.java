package com.ceos22.cgv_clone.common.redisson.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConcurrencyControl {

    String key();

    long waitTime() default 5L;
    long leaseTime() default 3L;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    int transactionTimeout() default 0;
}