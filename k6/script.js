import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
    stages: [
        { duration: "30s", target: 50 },
        { duration: "1m", target: 100 },
        { duration: "30s", target: 0 },
    ],
};

const BASE_URL = "https://localhost:8080";
const jar = http.cookieJar();

export default function () {
    const accessToken = __ENV.ACCESS_TOKEN;

    // 세션 쿠키 설정 (로그인 후 발급받은 쿠키 값으로 변경)
    jar.set(
        BASE_URL,
        "accessToken",
        accessToken,
        {
            domain: "localhost",
            path: "/",
        }
    );

    // 결제 요청
    const payload = JSON.stringify({
        movieTimeId: 501,
        screenSetId: 101,
        storeId: "CEOS-22-7654321",
        orderName: "영화예매",
        totalPayAmount: 15000,
        currency: "USD",
    });

    const headers = { "Content-Type": "application/json" };

    const res = http.post(`${BASE_URL}/api/reservations/seat`, payload, {
        headers,
        cookies: jar.cookiesForURL(BASE_URL),
    });

    check(res, {
        "status is 200": (r) => r.status === 200,
        "has reservationId": (r) => r.json("data.reservationId") !== undefined,
    });

    sleep(1);
}
