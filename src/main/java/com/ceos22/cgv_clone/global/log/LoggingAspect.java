package com.ceos22.cgv_clone.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Controller 진입/성공/실패
    @Around("execution(* com.ceos22.cgv_clone.domain..controller..*(..))") // controller 범위
    public Object logController(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis(); // 시간 카운트용
        String sig = pjp.getSignature().toShortString(); // 시그니처 문자열(클래스+메서드, 파라미터 개수 등)
        Map<String, Object> args = namedArgs(pjp);
        mask(args, "password", "pwd", "authorization", "token"); // 민감 정보 마스킹

        log.info("[CTRL-START] {} args={}", sig, args);
        try {
            Object ret = pjp.proceed(); // 로직 수행
            log.info("[CTRL-SUCCESS] {} took={}ms", sig, System.currentTimeMillis() - start);
            return ret;
        } catch (RuntimeException e) {
            log.warn("[CTRL-FAIL] {} took={}ms msg={}", sig, System.currentTimeMillis() - start, e.getMessage());
            throw e; // GlobalException에서 핸들링
        }
    }

    // Service 진입/성공/실패
    @Around("execution(* com.ceos22.cgv_clone.domain..service..*(..))")
    public Object logService(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String sig = pjp.getSignature().toShortString();
        log.debug("[SVC-START] {}", sig);
        try {
            Object ret = pjp.proceed();
            log.info("[SVC-SUCCESS] {} took={}ms", sig, System.currentTimeMillis() - start);
            return ret;
        } catch (RuntimeException e) {
            log.warn("[SVC-FAIL] {} took={}ms msg={}", sig, System.currentTimeMillis() - start, e.getMessage());
            throw e;
        }
    }

    // 유틸
    private Map<String, Object> namedArgs(ProceedingJoinPoint pjp) {
        CodeSignature cs = (CodeSignature) pjp.getSignature();
        String[] names = cs.getParameterNames();
        Object[] values = pjp.getArgs();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) map.put(names[i], values[i]);
        return map;
    }

    // 마스킹
    private void mask(Map<String, Object> args, String... keys) {
        for (String k : keys) if (args.containsKey(k)) args.put(k, "***");
    }
}
