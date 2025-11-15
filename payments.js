import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '2m', target: 100 },
        { duration: '2m', target: 200 },
        { duration: '2m', target: 400 },
        { duration: '2m', target: 600 },
        { duration: '2m', target: 800 },
        { duration: '2m', target: 1000 },
    ]
};

const BASE_URL = 'https://payment.loopz.co.kr';
const TOKEN = '{토큰}';

const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${TOKEN}`,
};

export default function () {
    // 고유한 paymentId 생성
    const paymentId = `order_${__VU}_${Date.now()}`;

    // 결제 요청
    const paymentPayload = JSON.stringify({
        storeId: 'CEOS-22-14097124',
        orderName: '노트북외 1건',
        totalPayAmount: 1500000,
        currency: 'KRW',
    });

    const paymentRes = http.post(
        `${BASE_URL}/payments/${paymentId}/instant`,
        paymentPayload,
        { headers }
    );

    // 결제 성공하면 조회
    if (paymentRes.status === 200) {
        sleep(1);
ccccccc
        http.get(
            `${BASE_URL}/payments/${paymentId}`,
            { headers }
        );
    }

    sleep(1);
}