import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '2m', target: 100 },
        { duration: '2m', target: 100 },
        { duration: '2m', target: 0 },
    ]
};

const BASE_URL = __ENV.SERVER_BASE_URL;
const TOKEN    = __ENV.USER_TOKEN;

const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${TOKEN}`,
};

export default function () {

    const paymentPayload = JSON.stringify({ orderId: 9 });
    const paymentRes = http.post(
        `${BASE_URL}/payments`,
        paymentPayload,
        { headers }
    );

    const paymentStatus = paymentRes.json('statusCode')

    if (paymentStatus === 201) {
        sleep(1);
        console.log(`⭕ 결제 성공 statusCode: ${paymentStatus}`);

    } else {
        console.error(`❌ 결제 실패 statusCode: ${paymentStatus}`);
    }

    sleep(1);
}
