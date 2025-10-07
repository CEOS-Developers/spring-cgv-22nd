# spring-cgv-22nd
## CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트
**영화관 예매 및 주문 시스템**을 구현하기 위한 데이터베이스 구조를 설계하였습니다.  
주요 기능은 영화관 탐색, 영화 탐색, 상영 스케줄 확인, 유저의 예매 및 주문, 찜하기입니다.

![img_1.png](img_1.png)

---

## 📦 테이블 구조

### 1. 영화관 (cinema)
- `cinema_id` (PK): 영화관 고유 ID
- `name`: 영화관 이름
- `region`: 지역
- `location`: 주소
- `description`: 영화관 상세 설명  

- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (cinema) : N (theater)**  
  ➡️ **1 (cinema) : N (order)**  
  ➡️ **1 (cinema) : N (theater_like)**

---

### 2. 상영관 (theater)
- `theater_id` (PK)
- `cinema_id` (FK → cinema)
- `name`: 상영관 이름
- `type`: 상영관 유형 (2D, 3D, IMAX, 4DX 등)
- `row`, `column`: 좌석 행/열
- `price`: 기본 좌석 가격  
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (theater) : N (schedule)**

---

### 3. 영화 (movie)
- `movie_id` (PK)
- `title` : 영화 제목
- `director` : 감독
- `genre`: 영화 장르
- `runtime`: 상영 시간
- `rating`: 관람 등급
- `release_date`: 개봉일
- `description`: 설명
- `total_audience`: 누적 관객 수 
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (movie) : N (schedule)**  
  ➡️ **1 (movie) : N (movie_like)**

---

### 4. 상영 스케줄 (schedule)
- `schedule_id` (PK)
- `movie_id` : 상영되는 영화의 id
- `theater_id` : 상영관 id
- `date`, `start_time`, `end_time`: 상영 일자 및 시간
- `is_morning`: 조조 여부
- `category`: 카테고리 (오전, 오후, 18시 이후, 심야)  
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (schedule) : N (reservation)**

---

### 5. 회원 (user)
- `user_id` (PK)
- `name`, `nickname`, `email`, `password`
- `grade`: 회원 등급
- `phone`: 전화번호  
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (user) : N (reservation)**  
  ➡️ **1 (user) : N (order)**  
  ➡️ **1 (user) : N (theater_like)**  
  ➡️ **1 (user) : N (movie_like)**

---

### 6. 예매 (reservation)
- `reservation_id` (PK)
- `schedule_id` : 예매한 영화 스케쥴 id
- `user_id` : 예약자 id
- `created_at`: 예약 시간
- `status`: 상태 (예매 완료, 취소)  
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (reservation) : N (reservation_seat)**

---

### 7. 예약 좌석 (reservation_seat)
- `reservation_seat_id` (PK)
- `reservation_id` : 예약 정보 id
- `schedule_id` : 예약된 영화 스케쥴 id
- `row`, `column`: 예약 좌석 위치

---

### 8. 메뉴 (menu)
- `menu_id` (PK)
- `name`: 상품 이름
- `price`: 가격
- `stock`: 재고
- `category`: 메뉴가 속한 카테고리 (콤보, 음료, 팝콘, 스낵, 굿즈)
- `short_description`, `description`: 메뉴 1줄 소개, 메뉴 설명  
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (menu) : N (order_item)**

---

### 9. 메뉴 주문 (order)
- `order_id` (PK)
- `cinema_id` : 주문이 이루어진 영화관 id
- `user_id` : 주문자 id
- `total_price`: 총 금액
- `created_at`: 주문 시각
- `status`: 결제 여부 (결제 대기, 결제 완료)
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **1 (order) : N (order_item)**

---

### 10. 주문 상품 (order_item)
- `order_item_id` (PK)
- `order_id` : 주문 id
- `menu_id` : 주문 메뉴 id
- `quantity`: 수량
- `unit_price`: 결제 시점 단가

---

### 11. 영화관 찜하기 (cinema_like)
- `cinema_like` (PK)
- `user_id` : 유저 id
- `cinema_id` : 영화관 id  
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **N:M** (user ↔ cinema)

---

### 12. 영화 찜하기 (movie_like)
- `movie_like_id` (PK)
- `user_id` : 유저 id
- `movie_id` : 영화 id   
- 다른 테이블과의 관계는 아래와 같습니다.  
  ➡️ **N:M** (user ↔ movie)

---

# 인증(Authentication) 방법 정리

## 1. 세션(Session) & 쿠키(Cookie) 인증

### 인증 흐름
1. 사용자가 로그인 요청을 보냅니다.
2. 서버는 사용자 정보를 확인한 뒤 **세션 ID**를 생성하고 세션 저장소에 기록합니다.
3. 서버는 Session ID를 쿠키에 담아 클라이언트로 전달합니다.
4. 클라이언트는 이후 요청마다 쿠키(Session ID)를 포함시킵니다.
5. 서버는 세션 저장소와 대조해 사용자를 확인하고 데이터를 반환합니다.

### 장점
- 실제 정보는 서버에 저장, 쿠키는 **세션 키(출입증)** 역할만 함 → 보안성 상대적으로 우수
- 매번 회원 정보를 확인하지 않아도 되므로 인증 속도가 빠름

### 단점
- 쿠키 탈취 시 **세션 하이재킹 공격** 발생 가능 → HTTPS + 세션 만료 시간 설정 필요
- 세션 저장을 위해 **서버 자원(메모리/DB 등)** 필요

---

## 2. JWT 기반 인증 (Access Token)

### 구조
- **Header**: 토큰 타입(JWT), 알고리즘(HS256 등)
- **Payload**: 사용자 ID, 권한, 만료시간 등 Claims
- **Signature**: 위·변조 방지용 서명 (Header + Payload + Secret Key 기반)

### 인증 흐름
1. 사용자가 로그인 요청을 보냅니다.
2. 서버는 사용자 정보를 확인 후 JWT(Access Token)를 생성합니다.
3. 클라이언트는 Access Token을 전달받아 저장합니다.
4. 이후 요청마다 `Authorization: Bearer <token>` 헤더에 토큰을 담아 전송합니다.
5. 서버는 토큰의 유효성(서명, 만료시간)을 확인 후 데이터를 반환합니다.

### 장점
- 서버 저장소가 불필요하고 **무상태(stateless)** 구조에 적합합니다.
- 다른 서비스와의 연동이 용이합니다.

### 단점
- 만료 전까지 강제 무효화 어렵습니다. (탈취 시 위험)
- Payload는 누구나 디코딩 가능하여 민감 정보 저장이 불가능합니다.
- 토큰 크기가 커서 요청 많을 시 트래픽 비용 증가합니다.

---

## 3. Access Token + Refresh Token 인증

### 개념
- **Access Token**: 짧은 유효기간(예: 1시간), API 요청시 인증/인가에 사용
- **Refresh Token**: 긴 유효기간(예: 2주), Access Token이 만료되었을 때 재발급 용도로 사용

### 인증 흐름
1. 로그인 성공 시 서버는 Access Token과 Refresh Token을 발급합니다.
2. 클라이언트는 Access Token을 요청에 포함하여 보냅니다.
3. Access Token이 만료되면 Refresh Token을 이용해 새로운 Access Token을 발급받습니다.
4. Refresh Token까지 만료되면 재로그인이 필요합니다.

### 장점
- Access Token을 짧게 가져가므로 탈취당해도 보안에 취약한 시간을 줄일 수 있습니다.
- 사용자는 자주 로그인할 필요가 없어 편리합니다.

### 단점
- 구현 복잡도가 올라갑니다.
- Access Token의 유효기간이 매우 짧은 경우 서버 부하 증가가 예상되며 매 API 호출마다 accessToken을 이용한 인증 부하가 발생합니다.

---

## 4. OAuth 2.0 인증

### 개념
- 외부 서비스(Google, Facebook 등) 계정을 사용해 인증을 위임받는 프로토콜
- 현재 표준은 **OAuth 2.0**입니다.

### 주요 참여자
- **Resource Owner**: 사용자
- **Client**: 우리의 애플리케이션
- **Authorization Server**: 인증/토큰 발급 서버
- **Resource Server**: 보호된 자원을 가진 서버

### 인증 흐름
1. 사용자가 Client에 로그인 요청
2. Client는 사용자를 Authorization Server(구글/페북 로그인 페이지 등)로 리디렉트
3. 사용자가 로그인 후 **Authorization Code**를 Client로 전달
4. Client는 Authorization Server에 Authorization Code를 전송해 **Access/Refresh Token** 발급
5. Client는 Access Token으로 Resource Server에 요청
6. 토큰 만료 시 Refresh Token으로 갱신

### 장점
- 소셜 로그인, 외부 계정 연동에 많이 활용되어 범용성이 높은 방법입니다.
- 표준화된 방식으로 다양한 서비스에서 실제로 활용하는 인증 방법입니다.

### 단점
- 설정이 복잡(redirect URI, client ID/secret 필요)하여 초기 구현 비용이 높습니다.
- 토큰 보관 및 만료 처리에 주의 필요합니다.

---

## 5. SNS 로그인 (Facebook, Google 등)

### 인증 흐름
1. 사용자가 서버에 로그인 요청
2. 서버는 SNS 로그인 URL을 클라이언트로 전달
3. 사용자가 해당 URL을 통해 로그인 → 인증 코드 반환
4. 서버는 Authorization Server에 코드 검증 요청 후 Access/Refresh Token + 사용자 정보 발급
5. 서버는 사용자 정보를 DB에 저장(신규면 회원가입, 기존이면 로그인)
6. 이후 인증은 세션/쿠키 또는 JWT 방식으로 관리  