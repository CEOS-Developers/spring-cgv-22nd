# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트
# ERD
<img width="2290" height="1322" alt="CGV_clone (2)" src="https://github.com/user-attachments/assets/7df82739-cf89-4cf3-b633-6504e9e33e1e" />

ERD는 다음과 같이 짜봤습니다

가장 크게 고민했던 사항은 두 가지였습니다.

1. 좌석 - 예매된 좌석의 관계

이전에는 상영관 - 좌석 - 예매 - 유저로 연결했으나, 이럴 경우 예매 상태를 seat에 담게될 경우, seat 테이블이 복잡해져 중간테이블(예매된 좌석)을 하나 더 만들었습니다. 처음에는 좌석 - 예매된 좌석을 일대다로 했으나, 특정 seat가 중복 예약되는 문제와 상태 관리 어려워 일대일로 변경했습니다

2. 매점테이블의 존재

원래는 매점 테이블을 만들었었으나, 생각해보니 영화관에 매점은 무조건 하나씩이고, 메뉴까지 다 같아 굳이 만들 필요가 없다는 걸 깨달았습니다. 따라서 이후 테이블을 삭제하고 영화관과 상품테이블을 일대다로 매핑했습니다.

---

### ERD 최종본
<img width="498" height="695" alt="Image" src="https://github.com/user-attachments/assets/92ab58bb-65ec-483a-9c9f-4710ac86ae28" />


# 내용정리

### JWT 인증 방법
JWT란, JSON Web Token의 약자로, 인증에 필요한 정보들을 토큰에 담아 암호화하여 인증에 사용하는 인터넷 표준 인증 방식이다.
.을 기준으로 HEADER, SIGNATURE, PAYLOAD로 구분되며 관련 정보들은 각각에 담긴 채 암호화되어있다.

즉 이는 일종의 확인서로, 우리가 한 사이트에 로그인을 해서 인증이 이루어지면, 서버는 이에 대한 확인서(jwt)를 우리에게 제공한다.
이후 우리는 서버에 요청을 할 때마다 서버에게 jwt를 함께 보여주면서 권한을 확인받는다.

JWT를 그대로 사용할 경우, 토큰 탈취의 위험성이 있어 주로 Access Token, Refresh Token 으로 나누어서 인증을 하는 방식으로
주로 사용된다.

✅Access Token : 클라이언트가 갖고있는 실제로 유저의 정보가 담긴 토큰으로, 클라이언트에서 요청이 오면 서버에서 해당 토큰에 있는 정보를 활용하여 사용자 정보에 맞게 응답을 진행한다. 탈취 위험을 줄이기 위해, 짧은 수명을 유지한다.

✅Refresh Token : 새로운 Access Token을 발급해주기 위해 사용하는 토큰으로 짧은 수명을 가지는 Access Token에게 새로운 토큰을 발급해주기 위해 사용된다.

<img width="682" height="420" alt="Image" src="https://github.com/user-attachments/assets/bb556967-3179-41ec-97bb-121147a3eb68" />
JWT 인증방식은 사진과 같다.

### Cookie 인증 방식
- Key, Value 쌍으로 이루어진 문자열로, 클라이언트가 웹사이트를 방문할 경우, 그 사이트가 사용하고 있는 서버를 통해
클라이언트의 브라우저에 설치되는 작은 데이터 조각이다.
- 다만 요청 시 쿠키의 값을 그대로 보내기때문에, 보안에 취약하다는 단점이 있다.

<img width="676" height="202" alt="Image" src="https://github.com/user-attachments/assets/d58e75d4-0eb2-4089-88f8-ee93a42f45df" />
쿠키 인증 방식이다.

### Session 인증 방식
- cookie의 보안적인 이슈 해결을 위한것으로, 세션은 민감한 인증 정보를 브라우저가 아닌, 서버측에서 저장하고 관리한다.
- 그러나 이 또한 세션ID를 탈취하여, 클라이언트로 위장할 수 있다는 단점이 존재한다.

<img width="694" height="203" alt="Image" src="https://github.com/user-attachments/assets/d97e0bff-5113-4860-b445-7e01080c6ef4" />
세션 인증 방식이다.

### Oauth 인증 방식
우선 Oauth란? 인터넷 사용자들이 다른 웹사이트 상의 자신의 정보에 대해 접근 권한을 부여할 수 있는 공통적인 수단으로서 사영되는
접근 위임을 위한 개방형 표준이다

-> 즉, 이를 이용한 인증 방식은, 한 어플리케이션을 이용할 때 사용자가 해당 어플리케이션이 아닌 외부 어플리케이션 (ex KAKAO, GOOGLE ...)의 Open API에서 로그인을 하여
해당 어플리케이션이 인증 과정을 처리해주는 인증 방식이다.

<img width="639" height="430" alt="Image" src="https://github.com/user-attachments/assets/ea5e67ab-743f-4562-b372-5017c330d634" />
Oauth 인증 방식이다.


### 아키텍처 구조도
<img width="414" height="370" alt="Image" src="https://github.com/user-attachments/assets/0c3c9956-5b5d-47ff-acfc-f95461d592ce" />

### 부하테스트 결과
<img width="802" height="351" alt="Image" src="https://github.com/user-attachments/assets/9367e673-d04f-4115-9665-c0305ee7dd60" />

결과 분석

총 요청 수: 200,670
최대 VU(가상 유저): 1,000명
평균 동시 접속자 (VU): 122명

✅p90, p95는 지연이 큼 -> 일부 요청이 지연된 상태
    p90: 전체 요청 중 90%가 해당 시간 내에 응답받는 시간
    p95: 전체 요청 중 95%가 해당 시간 내에 응답받는 시간
✅최댓값이 11초 -> 일부 요청이 매우 긴 시간동안 응답을 못했거나 기다림이 큰 상태

<img width="1382" height="562" alt="Image" src="https://github.com/user-attachments/assets/b55cd4f8-03b8-4fc9-9641-5dd4ea790dff" />

3시 15분부터 처리율이 400req/s 근처에서 증가하지 않음
Request Duration(p95)(파란색 면)이 급격히 상승 → 응답 지연 증가
-> 이 시점이 서버의 한계 처리량 (Throughput Ceiling)

<img width="1406" height="1174" alt="Image" src="https://github.com/user-attachments/assets/31760db2-8f70-4add-9036-3f0593893828" />

3시 15분부터 평균과 p90, p95, p99가 함께 급등함
-> 대부분의 요청이 여전히 처리되지만, 일부 요청이 오래 기다리며 지연이 누적되고 있음.

컨테이너들의 CPU 사용량 또한 확인해보고 싶었으나, 로컬에서 계속 뜨지않아서 해당 부분은 확인하지 못했습니다..

병목 가능성
1.	결제 생성 POST(쓰기 + 검증 + 좌석 확보 + 트랜잭션)
→ VU가 증가 시 처음 포화될 구간??
2.	DB 커넥션/트랜잭션/락 경쟁
→ 동시 쓰기 많으면 풀 고갈되고 대기 늘어남
3. 	조회 GET이 영향을 받는 구조(쓰기 직후 조회하면서 락/캐시 미스)
→ POST가 성공 직후 조회 시 POST가 느릴 때 조회도 함께 지연되지 않을까..