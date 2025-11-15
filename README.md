# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트

## 2주차 미션

### ERD
<img width="1830" height="1112" alt="CGV_HY" src="https://github.com/user-attachments/assets/32dd7a3b-e0aa-42bc-bfed-e4b7761a9fa8" />


### 설명
1. 영화관 & 상영관

   영화관(theater)은 이름과 주소를 가지고 있고, 그 안에는 여러 개의 상영관(screen)이 존재합니다.
   상영관은 일반관, 특별관 같은 타입을 구분할 수 있도록 하고, 좌석 수는 row_count와 col_count로 저장했습니다.
   처음에는 좌석을 아예 별도의 테이블로 관리할까 고민했지만, 특별관/일반관 모두 같은 직사각형 구조이고 속성이 단순해서 굳이 테이블을 분리하지 않아도 된다고 판단했습니다.

3. 예매 & 좌석

   예매(reservation)는 사용자가 특정 상영정보를 선택해 예약한 기록을 담습니다.
   문제는 한 번에 좌석을 여러 개 예약할 수 있다는 점이었는데, 이를 해결하기 위해 reservation_seat 테이블을 따로 두었습니다.
   이 안에는 예약 ID와 함께 row, col 좌표가 들어갑니다.
   이렇게 하면 동시 예약 상황에서도 좌석 단위로 관리할 수 있어 충돌을 줄일 수 있을거라 생각했습니다.

5. 매점

   매점(snack_menu)에는 상품명, 가격, 카테고리가 저장됩니다.
   재고는 처음에는 전역으로 관리할까 했지만, 지점마다 재고 상황이 다를 수 있어서 snack_inventory 테이블을 만들어 영화관별로 분리했습니다.
   사용자가 매점을 이용하면 snack_order가 생기고, 어떤 상품을 몇 개 샀는지는 order_detail에 기록됩니다.

<br><br>

### swagger 캡처 화면
<img width="1312" height="926" alt="KakaoTalk_Photo_2025-09-21-오후 8-23-19" src="https://github.com/user-attachments/assets/f14a8d8d-2dd9-40dd-888f-a8e305dc8f1f" />

## 3주차 미션

### 1)  쿠키(Cookie)

1. 개념
- 클라이언트의 브라우저에 저장되는 키-값 형태의 데이터입니다.
- 상태 유지가 필요할 때 서버가 클라이언트에게 전달하며, 클라이언트는 이후의 요청에서 이 데이터를 자동으로 서버에 전송합니다.

2. 특징
- 클라이언트 측 저장
- 만료 시간 설정 가능
- 브라우저 종료 후에도 유지 가능 (만료 시간에 따라)

3. 주요 구성 요소
- 이름(name), 값(value), 유효기간(Expires)
- 전송할 도메인(Domain)과 경로(Path)

4. 활용 예시
- 자동 로그인 (아이디 저장)
- 장바구니 데이터 유지


### 2) 세션(Session)

1. 개념
- 서버가 사용자별로 상태를 저장하고 관리하는 방식입니다.
- 서버는 클라이언트에게 고유한 세션 ID를 부여하며, 클라이언트는 이를 쿠키에 저장해 매 요청마다 서버로 전달합니다.

2. 특징
- 서버 측 저장
- 클라이언트 수가 많아지면 서버 자원 사용 증가
- 보안적으로 쿠키보다 안전함 (세션은 서버에서만 조작 가능)

3. 활용 예시
- 로그인 상태 유지

4. 다중 서버 환경에서의 세션 관리
- **Sticky Session**: 특정 사용자의 요청을 항상 동일한 서버로 전달
- **Session Clustering**: 모든 서버가 동일한 세션 정보를 공유
- **Session Storage**: Redis나 DB 등을 활용하여 세션을 중앙 집중식으로 관리

### 3) 토큰 기반 인증 (Token-based Authentication)

세션 방식은 서버 자원을 많이 소모하기 때문에, **토큰 기반 인증** 방식이 많이 활용됩니다.

1. 개념
- 서버가 클라이언트에게 고유 토큰을 발급하고, 클라이언트는 이후 요청마다 이 토큰을 헤더에 담아 전달합니다.
- 서버는 토큰의 유효성과 위변조 여부를 검증하여 인증을 처리합니다.

2. 특징
- 클라이언트에 저장됨으로 서버의 메모리 부담이 적음
- 서버는 Stateless 하게 동작 가능


### 4) JWT (JSON Web Token)

1. 개념
- 사용자 정보를 JSON 형태로 담아 인코딩한 토큰입니다.
- `.`을 기준으로 나뉜 세 부분으로 구성됩니다: **Header**, **Payload**, **Signature**

2. 구조
1. **Header**: 토큰 타입과 해시 알고리즘
2. **Payload**: 사용자 및 기타 데이터 (Claim)
3. **Signature**: 비밀키로 서명한 값으로 위변조 방지

3. 장점
- 자체적으로 필요한 정보를 포함 (Self-contained)
- 서버에 상태를 저장하지 않아도 됨
- Signature로 위조 여부 검증 가능



### 5) Access Token & Refresh Token

1. Access Token
- 서버가 인증에 성공한 클라이언트에게 발급
- 짧은 유효기간을 가지며 요청 시 포함되어 인증 처리됨
- Stateless 하므로 서버는 따로 저장하지 않음

2. Refresh Token
- Access Token의 단점을 보완하기 위해 함께 발급됨
- 유효기간이 길고, Access Token이 만료됐을 때 이를 통해 재발급 요청 가능
- 보안을 위해 서버에 저장되어 관리됨

3. 장점
- 탈취 시에도 유효기간이 짧아 피해 최소화 가능
- 유저 강제 로그아웃 또는 토큰 만료 구현 가능

4. 단점
- 서버에 저장소 필요 (세션 방식과 유사한 I/O 비용 발생)
- 클라이언트 구현이 복잡해짐 (재발급 로직 필요)



## 쿠키, 세션, 토큰 비교
| 항목        | 쿠키                        | 세션                     | 토큰 (JWT)            |
|-------------|-----------------------------|--------------------------|------------------------|
| 저장 위치   | 클라이언트                  | 서버                    | 클라이언트             |
| 보안성      | 낮음 (변조 가능)            | 높음 (서버 관리)         | 높음 (서명 기반 검증) |
| 서버 부담   | 없음                        | 있음                     | 없음                   |
| 속도        | 빠름                        | 느림                     | 빠름                   |
| 만료 제어   | 가능 (Expires 설정)         | 불명확                   | 가능 (Payload 내 설정) |
| 상태 관리   | O                           | O                        | X                      |


<br><br><br>

## 4주차 미션
### 동시성 해결 방법
1. synchronized

- 개념 : java에서 제공하는 synchronized를 사용해 동시성 문제를 해결하는 방법이다.
- 장점 : 
  - 구현이 간단하다
- 단점 : 
  - 하나의 프로세스에서만 동작하여 서버 1대에서만 유효하다. (분산 환경 불가)
  - @Transactional 어노테이션을 사용하면 트랜잭션이 종료되기 전에 다른 스레드에서 메서드에 접근할 수 있다. 
- 적용 상황 : 단일 서버

<br>

2. DB Lock

   (1) 비관적 락 (Pessimistic Lock)
      - 개념 : 데이터를 읽을 때부터 다른 트랜잭션의 접근을 막는다.
      - 장점 : 
        - 동시 수정 충돌을 확실히 방지할 수 있다. 
        - 즉시 일관성이 유지된다.
      - 단점 : 
        - 락 점유 시간이 길어지면 병목이 발생한다.
        - 트랜잭션 대기나 데드락이 발생할 가능성이 있다.
      - 적용 상황 : 
        - 트래픽이 많지 않은 서비스에 적합
   
   <br>
   
   (2) 낙관적 락 (Optimistic Lock)
      - 개념 : 읽을 때는 락을 걸지 않고, 저장 시 version 필드를 이용해 충돌을 감지한다.
      - 장점 : 데이터 조회시 Lock을 사용하지 않으므로 비관적 락보다 성능이 좋다.
      - 단점 : version 충돌이 자주 발생하면 성능이 떨어질 수 있다.
      - 적용 상황 : 
        - 트래픽이 많고, 동시 수정 확률 낮은 경우 적합

<br>

3. Redis

   (1) Lettuce
      - 개념 : setnx 명령어를 활용해 분산락을 구현하고, spin lock 방식을 사용하여 동시성 문제를 해결한다.
      - 장점 : 
        - spring-data-redis를 사용하면 기본으로 설정된 라이브러리이기 때문에 별도의 라이브러리를 사용하지 않아도 된다.
        - 구현이 간단하다.
      - 단점 : 
        - spin lock 방식으로 작동하기에 재요청 로직을 직접 구현해야 한다.
        - spin lock 방식을 사용하기에 redis에 부하를 줄 수 있다. -> Thread.sleep을 사용해 부하를 줄여줄 수 있다.
      - 적용 상황 : 락 획득 요청 재시도가 필요 없는 경우 적합
   
   <br>

   (2) Redisson
      - 개념 : pub-sub 기반 lock을 사용하여 동시성 문제를 해결한다. 
      - 장점 : 
        - Redisson은 채널에서 메시지를 확인하고 락 획득 요청을 하기 때문에 redis 부하가 적다.
      - 단점 : 
        - Lettuce에 비해 구현이 복잡하다.
        - 별도의 라이브러리를 사용해야 한다. 
      - 적용 상황 : 락 획득 요청 재시도가 필요한 경우 적합

<br><br>

### 클라이언트 도구 비교

1. Feign Client
: Spring Cloud에서 제공하는 선언형 HTTP 클라이언트

- 장점 :
  - 코드 간결
  - Spring Cloud LoadBalancer, Eureka 등과 자연스럽게 연동
  - 예외처리, 로깅 등 AOP 적용 용이
  - 유지보수 용이
- 단점 : 
  - 런타임 오버헤드 존재 (프록시 객체 생성으로 인한)
  - 복잡한 요청에 한계 존재
- 적용 상황 : 코드 가독성이 중요하거나, 선언적 방식의 인터페이스를 사용하는 것이 편리하다고 생각하는 경우 적합

<br>

2. RestTemplate
: Spring3에서 도입된 전통적 동기식 HTTP 클라이언트

- 장점 : 
  - 단순하고 직관적 (get/post 메소드 호출)
  - 안정적이며 레거시 코드에 폭넓게 사용
- 단점 : 
  - 동기 방식 (스레드 블로킹)
  - 2023년 이후 사실상 deprecated됨. WebClient로 대체하는 것이 권장됨.
- 적용 상황 : 동기식 호출에 적합한 클래식한 방법을 선호하는 경우 적합

<br>

3. Web Client
: Spring WebFlux의 비동기/논블로킹 HTTP 클라이언트, RestTemplate의 차세대 버전

- 장점 : 
  - 비동기/논블로킹으로 고성능 처리 가능
  - request/response 스트리밍 지원
  - Reactor 기반으로 복잡한 흐름 제어 가능
  - RestTemplate보다 확장성 높음
- 단점 : 
    - 리액티브 프로그래밍(Reactor) 학습 필요
    - 코드 가독성이 다소 떨어짐 (체이닝 구조)
    - 과도한 사용 시 복잡도 증가
- 적용 상황 : 리액티브 프로그레밍에 적합

<br><br>

### 로깅 전략
- 개발(dev): 콘솔 중심 + SQL, 요청/응답 디버깅
- 운영(prod): 파일 중심 + 핵심 비즈니스 로그만 INFO 이상
- 로그 레벨 구분:
  - ERROR: 예외 발생
  - WARN: 비정상 동작 (복구 가능)
  - INFO: 비즈니스 흐름
  - DEBUG: 상세 내부 동작 (개발 전용)

### 서비스 아키텍쳐
<img width="799" height="663" alt="Group 427320323" src="https://github.com/user-attachments/assets/0d3e7017-e7b4-406c-98d7-8ebb8b3eb923" />


## 7주차 미션
### 1. 트랜잭션 전파 속성 조사해보기
1) REQUIRED
- 의미 : 트랜잭션 필요
- 기존 트랜잭션 X : 새로 트랜잭션 생성
- 기존 트랜잭션 O : 기존 트랜잭션 참여

2) SUPPORTS
- 의미 : 트랜잭션 있으면 지원
- 기존 트랜잭션 X : 트랜잭션 없이 진행
- 기존 트랜잭션 O : 기존 트랜잭션 참여

3) MANDATORY
- 의미 : 트랜잭션 의무
- 기존 트랜잭션 X : IllegalTransactionStateException 예외 발생
- 기존 트랜잭션 O : 기존 트랜잭션 참여

4) REQUIRES_NEW
- 의미 : 항상 새로운 트랜잭션 필요
- 기존 트랜잭션 X : 새로운 트랜잭션 생성
- 기존 트랜잭션 O : 기존 트랜잭션 보류시키고 새로운 트랜잭션 생성

5) NOT_SUPPORTED
- 의미 : 트랜잭션 지원하지 않음
- 기존 트랜잭션 X : 트랜잭션 없이 진행
- 기존 트랜잭션 O : 기존 트랜잭션 보류시키고 트랜잭션 없이 진행

6) NEVER
- 의미 : 트랜잭션 사용하지 않음
- 기존 트랜잭션 X : 트랜잭션 없이 진행
- 기존 트랜잭션 O : IllegalTransactionStateException 예외 발생

7) NESTED
- 의미 : 중첩 트랜잭션 생성
- 기존 트랜잭션 X : 새로운 트랜잭션 생성
- 기존 트랜잭션 O : 중첩 트랜잭션 생성

### 2. 인덱스 자료구조

### 1) B+Tree (Balanced Plus Tree)
대부분의 관계형 데이터베이스(RDBMS)에서 사용하는 대표적인 인덱스 구조이다.
- 데이터를 **정렬된 상태**로 유지하며, **검색, 삽입, 삭제가 빠르게 수행**된다.
- 리프 노드(Leaf Node)들끼리 **Linked List 형태**로 연결되어 있어 범위 검색(`BETWEEN`, `ORDER BY`)에 유리하다.
- 이진 탐색(Binary Search) 방식으로 동작하며, 평균 탐색 시간은 **O(log N)**이다.

```sql
CREATE INDEX idx_user_name ON users(name);
```
위 코드는 users 테이블의 name 컬럼에 인덱스를 생성하여 검색 성능을 향상시킨다.

### 2) Hash Index (해시 인덱스)

Hash Index는 **Key-Value 형태**로 저장되며, 특정 키에 대해 빠르게 값을 찾을 수 있는 인덱스 구조이다.

- `=` 연산(정확한 값 일치 검색)에 최적화되어 있으며, 빠른 검색 속도를 제공한다.
- 그러나 데이터가 **정렬되지 않기 때문에**, 부등호(`<`, `>`, `BETWEEN`) 연산이 불가능하다.

<br><br>

### 3. 인덱스 종류 :  클러스터드 인덱스 vs. 비클러스터드 인덱스

- 인덱스 유형 비교

| 인덱스 유형 | 설명 |
|------------|--------------------------------------------------|
| **클러스터드 인덱스 (Clustered Index)** | 데이터를 정렬된 상태로 저장하며, 테이블 자체가 인덱스 역할을 한다. |
| **비클러스터드 인덱스 (Non-Clustered Index)** | 실제 데이터와 별도로 인덱스 테이블을 유지하며, 물리적 데이터 주소를 저장한다. |

---

**클러스터드 인덱스 특징**
- 한 **테이블당 하나만 생성 가능**하다.
- 테이블의 **데이터 자체가 정렬된 상태**로 저장된다.
- **검색 속도가 빠르지만**, 삽입(INSERT)·삭제(DELETE) 시 **데이터 재정렬이 필요**하여 성능이 저하될 수 있다.

---

**비클러스터드 인덱스 특징**
- 한 **테이블에 여러 개 생성 가능**하다.
- 실제 데이터와 **별도로 인덱스 테이블을 유지**하며, **물리적 데이터 주소를 저장**한다.
- 데이터 저장 순서와 무관하며, **검색 시 한 단계를 더 거쳐야 하므로 속도가 다소 느릴 수 있다.**

---

**클러스터드 인덱스 vs. 비클러스터드 인덱스 예제**

```sql
-- 클러스터드 인덱스 (기본키에 자동 생성됨)
CREATE TABLE users (
    id INT PRIMARY KEY,  -- 자동으로 클러스터드 인덱스 적용
    name VARCHAR(50)
);
```

```sql
-- 비클러스터드 인덱스 생성
CREATE INDEX idx_name ON users(name);
```
위 코드에서
- id 컬럼은 PRIMARY KEY로 설정되어 자동으로 클러스터드 인덱스가 생성된다.
- name 컬럼에는 비클러스터드 인덱스가 별도로 생성되어, 검색 성능을 향상시킬 수 있다.


<br><br>

### 4. 성능 최적화해보기
1) 인덱스 추가로 성능 최적화
   
[Before]

<img width="905" height="71" alt="image" src="https://github.com/user-attachments/assets/41fc1ffa-e129-4c62-9bd2-7b34b4342b59" />

[Optimization]

<img width="905" height="43" alt="image" src="https://github.com/user-attachments/assets/b01b8884-5065-4c03-8c0f-15bf84273ad0" />

[After]

<img width="905" height="69" alt="image" src="https://github.com/user-attachments/assets/63debba4-ac14-43ee-b38d-0fa1ae6692ce" />


<br><br>


2) INNER JOIN vs EXIST

[Before]

<img width="905" height="130" alt="image" src="https://github.com/user-attachments/assets/98967977-4bba-4352-99d7-b4c648990407" />


[After]

<img width="905" height="114" alt="image" src="https://github.com/user-attachments/assets/ad80f217-1e00-4b17-9df8-12e200a88e93" />


<br><br>


3) JOIN 순서 최적화
   
[Before]

<img width="881" height="114" alt="image" src="https://github.com/user-attachments/assets/c2941cee-4128-491c-b369-c7c40b01693c" />


[After]

<img width="881" height="109" alt="image" src="https://github.com/user-attachments/assets/df355b37-8562-4bf9-be64-a20759466ffc" />
