import http from "k6/http";
import { check, sleep } from "k6";
import { Trend, Rate, Counter } from "k6/metrics";

// 요걸로 실행했음  "C:\Program Files\k6\k6.exe" run .\purchase_test.js

export const options = {

    stages: [
        { duration: "2m", target: 100 },
        { duration: "2m", target: 200 },
        { duration: "2m", target: 400 },
        { duration: "2m", target: 600 },
        { duration: "2m", target: 800 },
        { duration: "2m", target: 1000 },
    ],

    thresholds: {
        http_req_failed: ["rate<0.01"],       // 전체 실패율 < 1%
        http_req_duration: ["p(95)<800"],     // 전체 95% < 800ms (부하 테스트면 여유있게)
        purchase_duration: ["p(95)<1500"],    // 구매 API 95% < 1.5s
        txn_duration: ["p(95)<2500"],         // E2E 트랜잭션(POST→검증) 95% < 2.5s
        http_req_waiting: ["p(95)<800"],      // 서버 처리시간
        http_req_connecting: ["p(95)<200"],   // 소켓/프록시 구간
    },
    summaryTrendStats: ["min","med","avg","p(90)","p(95)","max"],
};

/* ===================== 환경/상수 ===================== */
const baseURL = __ENV.BASE_URL || "http://43.200.188.243";
const token   = __ENV.TOKEN;
const path    = "/store/purchase";

const PRODUCT_IDS = (__ENV.PRODUCT_IDS || "1")
    .split(",")
    .map((s) => parseInt(s.trim(), 10))
    .filter((x) => Number.isFinite(x) && x >= 0);

// API 단위
const purchaseDuration = new Trend("purchase_duration"); // /store/purchase POST
const authErrors       = new Rate("auth_errors");

// 상태코드 분포
const s2xx = new Counter("status_2xx");
const s4xx = new Counter("status_4xx");
const s5xx = new Counter("status_5xx");

// 트랜잭션 단위
const txnStarted   = new Counter("txn_started");
const txnSucceeded = new Counter("txn_succeeded");
const txnFailed    = new Counter("txn_failed");
const txnDuration  = new Trend("txn_duration");

// 타이밍 분해
const t_blocked  = new Trend("http_t_blocked");       // res.timings.blocked
const t_connect  = new Trend("http_t_connecting");    // res.timings.connecting
const t_tls      = new Trend("http_t_tls");           // res.timings.tls_handshaking
const t_sending  = new Trend("http_t_sending");       // res.timings.sending
const t_waiting  = new Trend("http_t_waiting");       // res.timings.waiting (TTFB)
const t_receiving= new Trend("http_t_receiving");     // res.timings.receiving

// 상태코드별 카운터(원인 파악 쉬운 것 위주)
const s408 = new Counter("status_408");
const s429 = new Counter("status_429");
const s499 = new Counter("status_499"); // 클라이언트가 연결 끊음(프록시/타임아웃 연관)
const s500 = new Counter("status_500");
const s502 = new Counter("status_502");
const s503 = new Counter("status_503");
const s504 = new Counter("status_504");

// 네트워크 에러 코드 카운터(k6 Response.error_code)
const err_timeout = new Counter("error_timeout");
const err_reset   = new Counter("error_reset");
const err_refused = new Counter("error_refused");
const err_dns     = new Counter("error_dns");

// 비즈니스 성공률(HTTP 2xx라도 로직 실패 구분)
const biz_ok   = new Rate("biz_ok");
const biz_fail = new Rate("biz_fail");

// 실패 샘플 로그 제한
const MAX_FAILED_SAMPLES = 3;
let failedSamplesPrinted = 0;

function pick(arr) { return arr[Math.floor(Math.random() * arr.length)]; }
export default function () {
    if (!token) { throw new Error("TOKEN env var is required"); }

    const productId = PRODUCT_IDS.length ? pick(PRODUCT_IDS) : 1;

    // 트랜잭션 시작
    txnStarted.add(1);
    const t0 = Date.now();

    // 구매 요청
    const payload = JSON.stringify({
        items: [{ productId, quantity: 1 }],
        method: "CARD",
    });

    const res = http.post(`${baseURL}${path}`, payload, {
        headers: {
            accept: "application/json",
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
        tags: { endpoint: "purchase" },
    });

    // 상태코드 분포 집계
    if (res.status >= 200 && res.status < 300) s2xx.add(1);
    else if (res.status >= 400 && res.status < 500) s4xx.add(1);
    else if (res.status >= 500) s5xx.add(1);

    // 인증 실패율
    authErrors.add(res.status === 401 || res.status === 403);

    // 기본 체크(기능 테스트 관점)
    const ok = check(res, {
        "status is 2xx/201": (r) => r.status >= 200 && r.status < 300,
        "is JSON": (r) => { try { JSON.parse(r.body); return true; } catch { return false; } },
    });

    // 개별 API 시간 기록
    purchaseDuration.add(res.timings.duration);

    // 타이밍 분해 기록 (태그 포함하면 분해 분석 쉬움)
    const statusGroup = res.status ? Math.floor(res.status/100) + "xx" : "err";
    const outcome = (res.status >= 200 && res.status < 300) ? "ok" : "fail";

    t_blocked.add(res.timings.blocked,   { status_group: statusGroup, outcome });
    t_connect.add(res.timings.connecting,{ status_group: statusGroup, outcome });
    t_tls.add(res.timings.tls_handshaking,{ status_group: statusGroup, outcome });
    t_sending.add(res.timings.sending,   { status_group: statusGroup, outcome });
    t_waiting.add(res.timings.waiting,   { status_group: statusGroup, outcome });
    t_receiving.add(res.timings.receiving,{ status_group: statusGroup, outcome });

    switch (res.status) {
        case 408: s408.add(1); break;
        case 429: s429.add(1); break;
        case 499: s499.add(1); break;
        case 500: s500.add(1); break;
        case 502: s502.add(1); break;
        case 503: s503.add(1); break;
        case 504: s504.add(1); break;
        default: break;
    }

    if (res.error_code) {
        const e = String(res.error_code).toLowerCase();
        if (e.includes("timeout"))      err_timeout.add(1);
        else if (e.includes("reset"))   err_reset.add(1);
        else if (e.includes("refused")) err_refused.add(1);
        else if (e.includes("dns"))     err_dns.add(1);
    }

    let bodyJson;
    try { bodyJson = JSON.parse(res.body); } catch { bodyJson = null; }
    const businessOk = (res.status >= 200 && res.status < 300) && bodyJson && (
        bodyJson.orderId || bodyJson.id || bodyJson.success === true
    );
    biz_ok.add(businessOk);
    biz_fail.add(!businessOk);

    if (!ok && failedSamplesPrinted < MAX_FAILED_SAMPLES) {
        failedSamplesPrinted++;
        let body = res.body || "";
        if (body.length > 800) body = body.substring(0, 800) + "...(truncated)";
        console.warn(`\n--- FAILED SAMPLE #${failedSamplesPrinted} ---`
            + `\nstatus=${res.status}\nbody=${body}\n`);
    }

    // 트랜잭션 종료/집계
    const t1 = Date.now();
    txnDuration.add(t1 - t0);
    if (ok) txnSucceeded.add(1);
    else    txnFailed.add(1);

    // 과도한 폭격 방지
    sleep(1);
}

export function handleSummary(data) {

    const m = data.metrics;

    const txStarted   = m.txn_started?.values?.count || 0;
    const txSucceeded = m.txn_succeeded?.values?.count || 0;
    const txFailed    = m.txn_failed?.values?.count || 0;

    const s2 = m.status_2xx?.values?.count || 0;
    const s4 = m.status_4xx?.values?.count || 0;
    const s5 = m.status_5xx?.values?.count || 0;

    const txnP95 = m.txn_duration?.values?.p(95) || 0;
    const purP95 = m.purchase_duration?.values?.p(95) || 0;

    const summaryText =
        `=== CUSTOM SUMMARY ================================
Transactions
  started   : ${txStarted}
  succeeded : ${txSucceeded}
  failed    : ${txFailed}
  success%  : ${txStarted ? ((txSucceeded/txStarted)*100).toFixed(2) : "0"}%

Status codes
  2xx: ${s2}    4xx: ${s4}    5xx: ${s5}

Latency (ms)
  txn_duration p95      : ${txnP95.toFixed ? txnP95.toFixed(2) : txnP95}
  purchase_duration p95 : ${purP95.toFixed ? purP95.toFixed(2) : purP95}
===================================================\n`;

    return {
        "custom-summary.txt": summaryText
    };
}