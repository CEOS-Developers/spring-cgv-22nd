package com.ceos22.cgv_clone.global.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/*
    로깅에 들어갈 traceId 1개를 부여
    OncePerRequestFilter를 상속 받아 서블릿 체인 맨 앞쪽에서 요청당 1번 실행
 */
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "traceId";
    public static final String REQ_HEADER   = "X-Request-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // 클라이언트가 X-Request-Id 보내면 그걸 사용 or 새로 생성
        String incoming = req.getHeader(REQ_HEADER);
        String traceId = (incoming == null || incoming.isBlank())
                ? UUID.randomUUID().toString().substring(0, 8)
                : incoming;

        // 로그 MDC에 넣기
        MDC.put(TRACE_ID_KEY, traceId);

        // 응답 헤더에 넣기(다른 서비스가 다음 요청에서 재사용 가능)
        res.setHeader(REQ_HEADER, traceId);

        try {
            chain.doFilter(req, res);
        } finally {
            // 제거(스레드 재사용 시 메모리 누수 가능..)
            MDC.remove(TRACE_ID_KEY);
        }
    }
}
