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

---

# 동시성 문제
- 동시성 문제란, 여러 프로세스(혹은 스레드)가 공유자원(메모리, 파일, 소켓 등)에 동시에 접근할 때 발생하는 오류를 말합니다.
- Race Condition : 자원을 필요로하는 여러 프로세스나 스레드의 실행 순서에 따라 결과가 달라지는 현상
- 데이터 불일치 : 여러 프로세스가 동시에 데이터를 수정할 때 발생하는 문제
- 교착 상태(Deadlock) : 두 개 이상의 프로세스가 서로 상대방이 점유한 자원을 기다리며 무한 대기 상태에 빠지는 현상
- Starvation : 특정 프로세스가 자원을 할당받지 못해 무한 대기 상태에 빠지는 현상
- 원자성 문제 : 여러 연산이 하나의 단위로 처리되어야 하는데, 중간에 다른 프로세스가 개입하여 일관성이 깨지는 현상
- 갱신 손실 : 여러 프로세스가 동시에 데이터를 읽고 수정할 때, 마지막에 저장된 값이 이전 수정 내용을 덮어쓰는 현상
- 비일관성 읽기 : 한 프로세스가 데이터를 읽는 도중에 다른 프로세스가 그 데이터를 수정하여 일관성이 깨지는 현상
- 쓰레기 읽기 : 한 프로세스가 삭제되었거나 수정 중인 데이터를 읽는 현상

# OS 과목에서 제시하는 해결법
- 스핀락(Spinlock) : 짧은 시간 동안 자원을 기다리는 경우에 사용되는 락으로, 프로세스가 자원을 얻을 때까지 계속해서 체크합니다.
- 뮤텍스(Mutex) : 상호 배제를 위해 사용되는 락으로, 한 번에 하나의 프로세스만 공유 자원에 접근할 수 있도록 합니다.
```
# Peterson's Algorithm

i, j : process IDs (i ≠ j)
flag : critical section 진입 의사를 나타내는 boolean 배열
turn : 어느 프로세스의 차례인지를 나타내는 변수

repeat
  
  flag[i] := true; // i가 critical section에 진입하고자 함을 표시
  turn := j; // process j가 사용할 차례
  
  while (flag[j] == true and turn == j) do no-op; // j차례이고 j가 임계 구역에 진입하고자 하면 대기, 그 외에는 진입
    critical section
  flag[i] = false; // i가 critical section 사용이 끝났음을 표시
    
    ...   
    remainder section

until false;

```
- 세마포어(Semaphore) : 카운팅 락으로, 여러 프로세스가 동시에 접근할 수 있는 자원의 개수를 제한합니다.
```
type semaphore = record
    value : integer; // 세마포어의 현재 값
    L : queue of process; // 대기 중인 프로세스들의 큐
    end;

// 입장 요청
wait(S : semaphore) :
    S.value := S.value - 1;
    if S.value < 0 :
        add this process to S.L;
        block this process;
    end;

// 퇴장 처리 
signal(S : semaphore) :
    S.value := S.value + 1;
    if S.value <= 0 :
        remove a process P from S.L;
        wakeup(P);
    end;

```

- 모니터(Monitor) : 고수준의 동기화 메커니즘으로, 공유 자원에 대한 접근을 제어합니다.

# Spring에서 동기화 문제 해결법

## 1. synchronized
- 특정 코드 블록을 Mutual Exclusion 방식으로 감싸서 동일 시점의 진입을 1개로 제한하는 방법
- 애플리케이션 인스턴스가 1개이고
- 스프링의 경우 싱글톤 빈이 기본이므로 특정 빈에 대해 synchronized 키워드를 사용하면 해당 빈에 대한 동시 접근을 제한할 수 있다.
- 단, 분산 환경(다중 서버 or 다중 인스턴스)에서는 효과가 없다.

## 2. DB Lock
- DB 레벨에서 동시성 문제를 해결하는 방법

### 2.1 Pessimistic Lock
- 읽거나 수정할 레코드에 Lock을 걸어 다른 트랜젝션을 대기시킴
- 경합이 많은 경우 지연이나 병목이 발생할 수 있음
- 데드락이 발생할 수 있어 주의가 필요함
- 사용자 대기(결제 화면 등)처럼 긴 트랜젝션에는 부적합 (긴 대기시간)
- 단기 트랜젝션에 사용하는 것이 적합

### 2.2 Optimistic Lock
- 레코드에 `version`을 두고 수정 시점에 버전을 비교
- 버전이 다르면 다른 트랜젝션이 수정한 것으로 간주하고 예외 발생
- 경합이 적은 경우에 효과적이며, 데드락이 발생하지 않음
- 사용자 대기(결제 화면 등)처럼 긴 트랜젝션에도 적합
- 재시도 로직을 구현하여야 하며, 충돌이 많을 경우 재시도에 대한 비용이 커짐

### 2.3 Named Lock

## 3. Redis
- 싱글 스레드 기반
- 비교적 빠른 속도
- 

### 3.1 Lettuce
- 각 스레드가 lock 획득하기 위해 redis에 요청(setnx)
- spin lock 기반이므로 락을 확들할 때까지 재요청 하는 로직 작성 필요
  - redis에 부하가 걸릴 수 있음
- 락 획득 후 작업 수행
- 작업 완료 후 락 해제(del)
- 재시도가 필요하지 않을 경우 유리할 수 있음

### 3.2 Redisson
- Spin lock을 사용하지 않고도 분산 lock을 구현할 수 있는 방법 (Redis 부하가 적음)
- pub, sub 기반으로 락 획득을 (채널을 publish, subscribe하고 subscribe한 채널의 메세지를 일정시간동안 기대려 lock을 획득하는 구조)
- 시간을 지정하여 lock 획득을 대기하고 시간이 초과되면 lock 획득에 실패함 



예약 로직 정리

- 유저가 좌석을 예약 (여러 좌석을 한번에 받아옴)

이때 좌석에 대해서는 동시성 문제 해결이 필요함

- 좌석 예약을 3개 단계로 구분
  (HOLD, RESERVED, CANCELED)

Reddison에서 scheduleId, row, column을 조합한 key로 분산락을 구현

