import http from "k6/http";
import { sleep } from "k6";

export const options = {
    stages: [
        { duration: "1m", target: 100 },

        { duration: "3m", target: 100 },

        { duration: "1m", target: 0 },
    ],
};

export default function () {
    http.get("http://144.24.71.208:7777/api/movies");
    sleep(1);
}