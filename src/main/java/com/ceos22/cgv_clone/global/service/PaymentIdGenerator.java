package com.ceos22.cgv_clone.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PaymentIdGenerator {

    @Value("${payment.store-id}")
    private String storeId;

    private final RedisService redisService;

    public PaymentIdGenerator(RedisService redisService) {
        this.redisService = redisService;
    }

    //paymentId 생성: yyyyMMdd_xxxx 형식
    public String generate() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
        String key = "paymentId:" + storeId + ":" + date;

        // Redis에서 하루마다 카운터 증가
        long sequence = redisService.incrementWithExpire(key, 86400); //하루 지나면 만료

        //paymentId 생성
        String sequenceString = String.format("%04d", sequence);
        return date + "_" + sequenceString;
    }
}
