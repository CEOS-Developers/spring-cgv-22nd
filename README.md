# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트

---
## CGV Clone

### ERD
<img width="561" height="418" alt="Image" src="https://github.com/user-attachments/assets/60f501bf-ba70-4634-b41f-0d6c8d030143" />


### 명세

| 기능                   | Method | Endpoint | 인증 필요 |
|----------------------|--------|----------|--------|
| 영화 목록 조회             | `GET` | `/movies` | X |
| 특정 영화 상세 조회          | `GET` | `/movies/{movieId}` | X |
| 특정 영화가 상영하는 영화관 조회   | `GET` | `/movies/{movieId}/theaters` | X |
| 특정 영화-영화관의 상영 시간표 조회 | `GET` | `/movies/{movieId}/theaters/{theaterId}/showtimes` | X |
| 극장 목록 조회             | `GET` | `/theaters` | X |
| 극장 상세 조회             | `GET` | `/theaters/{id}` | X |
| 특정 상영 시간표 좌석 조회      | `GET` | `/showtimes/{showtimeId}/seats` | X |
| 회원가입                 | `POST` | `/auth/signup` | X |
| 로그인                  | `POST` | `/auth/login` | X |
| 영화 찜 추가              | `POST` | `/favorites/movies/{movieId}` | O |
| 영화 찜 취소              | `DELETE` | `/favorites/movies/{movieId}` | O |
| 영화관 찜 추가             | `POST` | `/favorites/theaters/{theaterId}` | O |
| 영화관 찜 취소             | `DELETE` | `/favorites/theaters/{theaterId}` | O |
| 티켓 예매                | `POST` | `/tickets/reserve` | O |
| 티켓 취소                | `DELETE` | `/tickets/cancel/{ticketId}` | O |
| 주문 생성                | `POST` | `/orders` | O |
| 주문 조회                | `GET` | `/orders/{orderId}` | O |

### 예매

- 예매 시 여러 좌석을 한 번에 예약할 수 있다.
- 예매 후에 부분 취소는 불가하다.
- 예매 취소 내역이 남지 않는다.
- 좌석의 중복 예매는 불가능하다.

- #### 예매 과정
    1. 영화 선택

       보고싶은 영화 선택(`/movies` or `/movies/{movieID}`)
    2. 영화관 선택

       선택한 영화를 상영하는 극장 조회 및 선택(`/movies/{movieId}/theaters`)
    3. 상영 시간표 조회(`/movies/{movieId}/theaters/{theaterId}/showtimes`)
    4. 좌석 조회

       예매 가능한 좌석 조회(`/showtimes/{showtimeId}/seats`)
    5. 좌석 예매

       청소년/성인 인원 수, 좌석 선택 후 예매(`/tickets/reserve`)

### 주문

- 여러 상품을 한 번에 주문할 수 있다.
- 주문 취소 기능은 제공하지 않는다.
- 주문 시 해당 상품의 재고가 차감된다.
- 재고가 부족하면 주문이 거절된다.
- 총 결제 금액은 `상품 가격 × 수량`으로 계산된다.

- #### 주문 과정
    1. 상품 조회

       극장 내 매점 상품 확인 (`/products`)
    2. 주문 생성

       특정 매점의 상품을 주문(`/orders`)
    3. 주문 조회

       생성된 주문 내역 확인(`/orders/{orderId}`)
---
## 2주차 추가 조사

1. data jpa를 찾다보면 SimpleJpaRepository에서  entity manager를 생성자 주입을 통해서 주입 받는다. 근데 싱글톤 객체는 한번만 할당을  받는데, 한번 연결 때 마다 생성이 되는 entity manager를 생성자 주입을 통해서 받는 것은 수상하지 않는가? 어떻게 되는 것일까? 한번 알아보자

   Spring은 실제 `EntityManager` 객체를 직접 주입하는게 아니라 `EntityManager Proxy`를 주입. 이 프록시는 현재 트랜잭션에 맞는 실제 `EntityManager`를 찾아서 위임. 따라서 Repository는 싱글톤이면서도, 매 요청마다 올바른 `EntityManager`를 사용할 수 있음.
2. fetch join 할 때 distinct를 안하면 생길 수 있는 문제

   OneToMany 관계에서 Fetch Join을 쓰면, 엔티티가 중복되어 조회되는 문제가 발생한다.

   ex) Team-Member 관계에서, 팀에 속한 멤버가 여러명일 때 Fetch Join하면 같은 Team 객체가 중복 발생. (아래는 TeamA에 속한 멤버가 2명, TeamB에 속한 멤버가 1명인 경우)
   ```java
    String queryA = "select t from Team t join fetch t.members";
    List<Team> resultListA = em.createQuery(queryA, Team.class)
            .getResultList();
    for (Team team : resultListA) {
        System.out.println("team.getName() = " + team.getName() +
                ", team.getMembers().size() = " + team.getMembers().size());
    }

    String queryB = "select distinct t from Team t join fetch t.members";
    List<Team> resultListB = em.createQuery(queryB, Team.class)
            .getResultList();
    for (Team team : resultListB) {
        System.out.println("team.getName() = " + team.getName() +
                ", team.getMembers().size() = " + team.getMembers().size());
    }
   ```

   ### queryA 실행 시 결과:

   team.getName() = TeamA, team.getMembers().size() = 2

   team.getName() = TeamA, team.getMembers().size() = 2

   team.getName() = TeamB, team.getMembers().size() = 1

   ### queryB 실행 시 결과:

   team.getName() = TeamA, team.getMembers().size() = 2

   team.getName() = TeamB, team.getMembers().size() = 1
---
## 3주차 내용 정리

### JWT
- 사용자를 인증하고 식별하기 위한 정보들을 암호화시킨 토큰.
- JSON 데이터를 URL로 이용할 수 있는 문자(Base64 URL-safe Encode)로 인코딩하여 직렬화한 것.
- 전자 서명도 있어 JSON의 변조를 체크할 수 있음.
- 쿠키를 통해 클라이언트에 저장.
- 단순히 HTTP 요청시 헤더에 토큰을 첨부하는 것만으로 데이터를 요청하고 응답을 받아올 수 있음.
- '.'을 기준으로 Header, Payload, Signature 나누어짐.

- <img width="660" height="275" alt="Image" src="https://github.com/user-attachments/assets/75968804-0356-49dc-a9b0-96bb47b7616a" />



  #### AccessToken

  - 일정 시간 동안 사용자의 인증과 권한을 보장하는 토큰(클라이언트가 인증 정보를 보관)

AccessToken을 이용한 로그인 방식
  1. 사용자 로그인
2. 사용자 확인 후, 사용자의 고유한 ID값 부여하고 Payload에 정보를 넣음
3. JWT 토큰의 유효기간을 설정
4. SECRET KEY를 통해 암호화된 Acess Token을 HTTP 응답 헤더에 실어 보냄
5. 사용자는 Acess Token을 받아 저장한 후, 인증이 필요한 요청마다 토큰을 HTTP 요청 헤더에 실어서 보냄
6. 서버에서는 해당 토큰의 verify Signature를 SECRET KEY로 복호화한 후, 조작 여부, 유효 기간을 확인
7. 검증이 완료되면, Payload를 디코딩하여 사용자의 ID에 맞는 데이터를 가져옴

장점: 토큰 발급 후 검증만 거칯면 되기에 추가 저장소 필요 X, 확장성이 뛰어남

단점: 유효기간이 만료되기 전까지 토큰의 삭제가 불가능, Payload의 정보는 누구나 접근할 수 있기에 중요한 정보 보관 힘듬 등

  #### Refresh Token
  - AccessToken을 재발급하기 위한 토큰(AccessToken은 짧은 만료 기간, RefreshToken은 긴 만료 기간으로 설정)

  - AccessToken 만료 시, RefreshToken을 이용해 서버에서 새로운 AccessToken을 발급받을 수 있음.
  - AccessToken의 탈취 문제를 보완하기 위해 이용.

  #### JWT 인증 방식(AccesToken + RefreshToken)
1. 사용자 로그인
2. 서버는 회원 확인 후 서명된 JWT 생성하여 클라이언트에 응답
3. 이때 Access Token과 Refresh Token을 같이 전달
4. 사용자가 요청할 때마다 Access Token와 함께 보냄
5. 서버에서 Access Token을 검증
6. 검증이 완료되면 응답을 보낸다.
7. Access Token 만료
8. 사용자가 Access Token과 함께 데이터를 요청
9. 서버에서 Access Token이 만료된 것을 확인
10. 만료되었다는 것을 알려주는 응답을 보냄
11. 사용자는 만료 응답을 받고 Access Token과 Refresh Token을 같이 담아 발급 요청을 보냄
12. Refresh Token을 확인한 후 Access Token을 발급
13. Access Token과 함께 응답을 보냄
  
장점: AcessToken의 유효 기간이 짧기 때문에, AccessToken만을 이용한 인증보다는 안전

단점: 구현이 복잡, AccessToken 재발급마다 서버의 자원 낭비

  #### RefreshToken 탈취 대응
  RefreshToken의 만료 기간이 길기에 이게 탈취된다면 해커가 지속적으로 AccessToken을 발급받을 수 있는 문제 발생. 이를 위해 다음과 같은 방법을 씀

  - RTR(Refresh Token Rotation) + 서버 저장소를 활용한 유효성 검증

    AccessToken 재발급 시 RefreshToken도 재발급(이때 기존 RefreshToken은 무효화)
    
    서버 저장소에 RefreshToken 기록하여, 재발급 요청 시 유효성 검증
    
    ex) `AccessToken - Cookie, RefreshToken - Cookie + Radis`, `AccessToken - 메모리, RefreshToken - Cookie + Radis(추천!!)`


### 쿠키
- Key/Value 쌍으로 이루어진 문자열
- 사용자 브라우저에 저장.
- 4KB 이하의 한정적인 저장 공간으로 용량이 제한됨.
- 브라우저마다 쿠키 지원 형태가 달라 브라우저간 공유가 불가능.
- 보안에 취약하다는 단점(요청시 쿠키의 값을 그대로 보내기 때문).
- httpOnly flag로 클라이언트 단에서의 접근으로부터는 보호 가능.
- 요청마다 쿠키를 담아 보내므로 쿠키 사이즈가 커지면 네트워크 부하가 심해짐.

#### 쿠키 인증 방식
1. 맨 처음에 클라이언트는 쿠키없이 요청을 보냄
2. 서버는 이에 대한 응답을 할 때 클라이언트에 저장하고 싶은 정보를 응답 헤더의 Set-cookie에 담음
3. 이후 클라이언트는 요청을 보낼 때마다 요청 헤더의 Cookie에 저장된 쿠키를 담아 보냄
4. 서버는 쿠키에 담긴 정보를 통해 클라인언트가 누군지 식별

### 세션
- Key/Value 쌍으로 이루어짐.
- 쿠키가 보안에 취약하기에 비밀번호같은 민감한 인증 정보를 브라우저가 아닌 서버 측에 저장하고 관리하는 것.
- 서버 메모리나 서버 로컬 파일 또는 데이터 베이스에 저장(세션 저장소에 저장, 추가적인 저장 공간이 필요).
- 사용자 식별자인 session id를 저장과 정보를 저장.
- 사용자가 많아지면 정보를 찾는 데이터 매칭에 오랜 시간이 걸리면서 부하가 가해짐.
- 쿠키에 session id를 저장.
- 위의 쿠키가 노출되더라도 session id가 개인정보를 가지지 않아 1번째 쿠키 인증보다는 안전하지만 해커가 세션 ID 자체를 탈취하여 위장하여 접근할 수 있다는 한계가 있음.(하이재킹 공격)

#### 세션 인증 방식
1. 사용자가 로그인한다.
2. 서버는 회원 확인 후 세션 저장소에 Session ID를 저장
3. 로그인에 대한 응답으로 Cookie에 Session ID를 담아 전달
4. 사용자가 요청할 때마다 Session ID가 담긴 Cookie와 함께 보냄
5. 서버는 세션 저장소에 Session ID와 일치하는 지 확인
6. Session ID가 일치하면 응답을 보냄


### OAuth(Open Authorization)
- OAuth는 제3자 애플리케이션이 사용자 자원에 제한된 접근을 허용하면서, 사용자 패스워드를 노출하지 않고 인증 및 권한 위임을 할 수 있게 하는 표준 프로토콜 
- ex) 소셜 로그인, API 권한 위임 등

#### OAuth 인증 방식

<img width="737" height="517" alt="Image" src="https://github.com/user-attachments/assets/1577b1cc-e464-494b-8f24-d012616c4c51" />

  1. 자원 소유자(사용자)가 구글 로그인을 요청
  2. 클라이언트는 인증 서버에 로그인 페이지를 요청
  3. 인증 서버가 로그인 페이지를 제공
  4. 제공받은 로그인 페이지에 ID와 비밀번호를 입력
  5. 입력받은 값으로 인증 서버에 요청
  6. 인증 서버에 Authorization code를 발급
  7. 이 code로 인증 서버에 Access Token를 요청
  8. 인증 서버에서 Access Token을 발급
  9. 인증 완료
  10. 자원 서버에 Access Token을 담아 데이터를 요청
  11. Access Token을 검증 후 응답
---
## 4주차

### 동시성 해결 방법

#### 1. synchronized
synchronized는 멀티스레드 환경에서 동시에 하나의 자원(객체, 변수, 메서드 등)에 접근하는 것을 막는 키워드

   ```java
    // 1. 메서드 전체에 적용
    
    public synchronized void updateStock() {
        stock--;
    }
    -> 한 스레드가 updateStock()을 실행 중이면 다른 스레드는 이 메서드가 끝날 때까지 대기
        
    // 2. 코드 블록에 적용
    public void updateStock() {
        synchronized (this) {
            stock--;
        }
    }
    
    // 3. 특정 객체 기준으로 적용
    synchronized (someObject) {
    }
   ```

위와 같이 메서드 전체, 코드 블록, 특정 객체에 synchronized를 적용할 수 있다.

장점: 구현이 간단하고 별도의 설정 없이 바로 적용 가능, 메서드/블록 단위로 직관적인 제어

단점: 서버가 멀티 인스턴스일 경우 `인스턴스 간 락 공유 불가능`

-> why? synchronized는 JVM 내부에서만 유효! 그러나 각 서버의 JVM은 메모리를 공유하지 않기에 락이 공유되지 않음


#### 2. DB Lock
application level이 아닌 `데이터베이스에서 직접 Lock`을 제어하는 방식



- `Permission Lock` (비관적 락: 다 접근하지마)

    데이터 조회 시점에 DB에 실제 락을 걸어 다른 트랜잭션이 해당 행(row)을 수정하지 못하도록 막는 방식
    
    주로  `SELECT ... FOR UPDATE `를 사용

    예시) findByIdForUpdate() 호출 시 해당 row에 쓰기 락을 건다.(다른 트랜잭션이 접근 불가)
    
    // LockModeType.PESSIMISTIC_WRITE: exclusive lock (읽기/쓰기 모두 잠금)

    // LockModeType.PESSIMISTIC_READ: shared lock(쓰기 잠금)
        
    ```java
        public interface StockRepository extends JpaRepository<Stock, Long> {
            @Lock(LockModeType.PESSIMISTIC_WRITE) // SELECT !FOR UPDATE 쿼리가 실행(읽기, 쓰기 모두 잠금)
            @Query("SELECT s FROM Stock s WHERE s.id = :id")
            Optional<Stock> findByIdForUpdate(@Param("id") Long id);
        }
     ```
    
    ```java
        @Transactional
        public void decreaseStock(Long id) {
            Stock stock = stockRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
        
            if (stock.getQuantity() <= 0) {
                throw new IllegalStateException("재고 부족");
            }
        
            stock.decrease(); // 수량 차감
        }
    
    ```
    
    장점: 간단하고 직관적, 데이터 정합성 100% 보장

    단점: 트랜잭션이 오래 유지되면 `데드락` 위험, `성능 저하(동시 처리량 감소)`

  -> why? (아래 예시 참고)
    ```sql
    -- 트랜잭션 A
    BEGIN;
    SELECT * FROM product WHERE id = 1 FOR UPDATE;  -- product(1) 잠금
    UPDATE product SET stock = stock - 1 WHERE id = 2; -- product(2) 수정 시도 (대기)
  
    -- 트랜잭션 B
    BEGIN;
    SELECT * FROM product WHERE id = 2 FOR UPDATE;  -- product(2) 잠금
    UPDATE product SET stock = stock - 1 WHERE id = 1; -- product(1) 수정 시도 (대기)
    ```
  트랜잭션 A는 1번 상품을 먼저 락 걸고, 나중에 2번 상품을 수정하려고 함

  트랜잭션 B는 2번 상품을 먼저 락 걸고, 나중에 1번 상품을 수정하려고 함

  -> 서로가 상대방의 락이 풀리길 기다리면서 Deadlock 발생
  -> 트랜잭션이 오래 유지 열려 있으면, 락이 오래 유지되고, 락을 기다리는 대기 트랜잭션이 많아짐


- `Optimistic Lock`(낙관적 락: 일단 접근 OK, 충돌나면 그때 확인)

    실제 락을 이용하는 방식이 아닌 버전과 같은 칼럼을 추가하는 방법

    트랜잭션 간의 동시 접근을 허용하고, 커밋 시점에 데이터 정합성 이슈가 발견되면 그때 롤백을 수행

    ```java
    @Entity
    public class Stock {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        private int quantity;
    
        @Version  // version 관리용 필드
        private Long version;
        
    }
    ```
    ```sql
    -- 조회
    SELECT id, quantity, version FROM stock WHERE id = 1;
    
    -- 업데이트 (커밋 시점)
    UPDATE stock
    SET quantity = ?, version = version + 1
    WHERE id = ? AND version = ?;
    ```
  엔티티에 @Version 필드를 추가하면 JPA가 내부적으로 다음 로직을 수행

    1. 데이터를 조회할 때 현재 version 값을 읽어둠.
    
    2. 데이터를 수정하고 commit할 때,
    UPDATE ... WHERE version = ? 조건으로 업데이트를 시도.
    
    3. 만약 version이 바뀌었으면 ->  충돌 감지 -> OptimisticLockException 발생 -> 롤백

  ######   ❗시나리오

    | 시간     | 트랜잭션 A                      | 트랜잭션 B                                                       |
    | ------ | --------------------------- | ------------------------------------------------------------ |
    | **t1** | `Stock(id=1, version=1)` 조회 | `Stock(id=1, version=1)` 조회                                  |
    | **t2** | 재고 차감 (`quantity=9`)        | 재고 차감 (`quantity=9`)                                         |
    | **t3** | 커밋 → `version=2`로 증가        |                                                              |
    | **t4** |                             | 커밋 시도 → `version=1` 조건 불일치 → **OptimisticLockException 발생** |

  장점: DB 락을 사용하지 않아 `성능`이 좋고 `데드락 발생 X`, 읽기 비율이 높은 시스템에 유리

  단점: 충돌 발생 시 예외(OptimisticLockException)로 재시도 필요, 동시 수정이 잦은 환경에서는 실패 빈번

  - `Named Lock`

      MySQL의 `GET_LOCK()` / `RELEASE_LOCK()` 같은 함수를 이용해서 “특정 문자열 이름을 가진 락”을 명시적으로 획득하고 해제하는 방식

      같은 이름의 락을 요청한 트랜잭션끼리는 동시에 실행되지 않게 하자!
    
      - **GET_LOCK(str, timeout)**

          입력받은 이름(str)으로 timeout초 동안 Lock 획득 시도
  
          timeout이 음수 → Lock을 획득할 때까지 무한 대기
  
          한 세션에서만이 잠금 점유 가능
  
          Transaction의 커밋이나 롤백의 영향을 받지 않음

          Return: 1(성공), 0(실패), null(에러)
      - **RELEASE_LOCK(str)**

        입력받은 이름(str)의 Lock 해제

        Return: 1(해제 성공), 0(현재 스레드에서 획득한 Lock이 아님), null(Lock이 존재하지 않음)

      ``` java
      @Repository
      public class NamedLockRepository {
      @PersistenceContext
      private EntityManager em;
    
          // 락 획득 시도 (timeout 초까지 대기)
          public void getLock(String lockName) {
              em.createNativeQuery("SELECT GET_LOCK(:lockName, 3000)")
                      .setParameter("lockName", lockName)
                      .getSingleResult();
          }
    
          // 락 해제
          public void releaseLock(String lockName) {
              em.createNativeQuery("SELECT RELEASE_LOCK(:lockName)")
                      .setParameter("lockName", lockName)
                      .getSingleResult();
          }
      }
      ```
      ```java
      @Service
      @RequiredArgsConstructor
      public class OrderService {

          private final NamedLockRepository lockRepository;
          private final StockService stockService;
    
          @Transactional
          public void decreaseStock(Long productId) {
              String lockKey = "lock:product:" + productId;
    
              try {
                  lockRepository.getLock(lockKey);
                  stockService.decrease(productId); // 실제 로직 수행
              } finally {
                  lockRepository.releaseLock(lockKey);
              }
          }
      }
      ```
    ######   ❗시나리오
    | 트랜잭션 | 수행 순서                                          |
    | ---- | ---------------------------------------------- |
    | A    | `GET_LOCK('lock:product:1')` → 성공 → 재고 감소 수행   |
    | B    | `GET_LOCK('lock:product:1')` → A가 락 해제될 때까지 대기 |
    | A    | `RELEASE_LOCK('lock:product:1')` → 락 해제        |
    | B    | 락 획득 후 로직 수행                                   |

    장점: 트랜잭션 단위로 명시적 락 관리 가능, 구현이 단순하고 DB에서 직접 관리

    단점: DB 커넥션이 끊기면 락도 해제, 락을 너무 많이 사용하면 DB 부하 증가, 분산 환경에서는 동작 불가

#### 3. Redis
멀티 서버 환경에서의 동시성 문제를 해결하기 위해 사용되는 Redis 기반의 방식

|       | Redis 기반 분산 락                                               |
| ----- | ----------------------------------------------------------- |
| 개념    | Redis의 원자적 명령(SETNX 등)을 이용해 특정 키에 대해 하나의 프로세스만 접근하도록 제어하는 락 |
| 특징    | DB 락은 하나의 DB 세션 안에서만 유효하지만, Redis 락은 **서버 간 전역 공유 가능**      |
| 사용 라이브러리 | `Spring Data Redis` (Lettuce 클라이언트) / `Redisson`            |


- `Lettuce`

    Spring Data Redis에서 사용하는 기본 클라이언트인 Lettuce를 이용한 락

    Redis의 SETNX(set if not exists) 명령을 활용해서 특정 key가 없을 때만 락을 걸고, 일정 시간이 지나면 자동으로 해제

    -> 이를 통해 락이 이미 사용중이면 주기적으로 락을 획득하기 위해 요청하는 `스핀락`을 구현

    <img width="729" height="565" alt="Image" src="https://github.com/user-attachments/assets/db543a7a-9898-47a6-acb3-7e3b00b60589" />

  ######   ❗동작방식

    1. SET stock-id "lock" NX EX 3
    
        -> “stock-id” 이라는 키가 없을 때만 생성
        
        -> 만료시간 3초 설정 (자동 해제)
    
    2. 다른 프로세스가 같은 키로 락을 요청하면 실패(락을 획득할 때까지 요청)
    
    3. 작업 완료 후 키 삭제 (DEL stock-id)

  장점: 구현이 단순하고 빠름, Redis의 TTL 덕분에 락 자동 해제, 여러 서버 간 전역 락 가능

  단점: 락을 획득할 때까지 계속 요청을 시도하기 때문에 레디스 서버에 부하를 주는 방식


- `Redisson`

  Redisson은 Redis 위에 구현된 고수준 분산 락 라이브러리

  Pub-sub 기반의 Lock 구현을 제공 하여 메시지 브로커 기능을 통해 락을 획득

  즉, 락을 점유 중인 스레드가 락을 해제했을 때 대기 중인 스레드에게 알려주어 다음에 락 획득을 다시 시도

  <img width="661" height="561" alt="Image" src="https://github.com/user-attachments/assets/f65e8056-3c3c-46c1-acf7-2a9bf7fd501e" />
 
  ######   ❗동작방식
    1. 락 획득 (tryLock)
    
        Application A가 Redis에 락을 요청한다.
    
        성공 시 A는 해당 자원에 대한 접근 권한을 얻는다.
    
        실패 시, Redisson은 내부적으로 락 해제 채널(redisson_lock_channel) 을 구독한다.
    
    2. 락 해제 (unlock)
    
        A가 작업을 마치고 락을 해제하면, Redisson이 Redis에 publish 명령을 보낸다.
    
        해당 메시지는 redisson_lock_channel을 구독하고 있던 다른 스레드(B 등)에 전달된다.
    
    3. 락 재시도
    
        메시지를 받은 B는 곧바로 락 획득을 다시 시도한다.
    
        따라서 불필요한 polling 없이 즉시 락 획득 가능하다.

  장점: 분산 환경에서도 안정적, pub/sub 기반으로 인한 CPU/네트워크 부하 감소

  단점: 라이브러리 의존성 높음, Redis 노드 다수 구성 필요 시 복잡도 증가

#### 4. 사용 추천 환경 정리

| 구분                        | 사용 추천 환경                                                      |
| ------------------------- | ------------------------------------------------------------- |
| **synchronized**          | 단일 서버 환경<br>트래픽이 많지 않은 서비스<br>간단한 동기화만 필요한 경우                 |
| **DB Lock – Pessimistic** | 동시에 동일 데이터를 자주 수정하는 환경<br>정합성이 매우 중요한 트랜잭션 처리 시스템             |
| **DB Lock – Optimistic**  | 충돌 가능성이 낮은 환경<br>읽기 위주 서비스<br>충돌 발생 시 재시도가 가능할 때              |
| **DB Lock – Named Lock**  | 트랜잭션 외부에서 간단히 임계영역을 보호해야 할 때<br>잠금 유지 시간이 짧은 경우               |
| **Redis (Lettuce)**       | 다중 서버 환경이지만 단순한 락 로직일 때<br>락 유지 시간이 짧고 빠른 응답이 필요한 서비스         |
| **Redis (Redisson)**      | 다중 서버·분산 환경에서 자원 경쟁이 심한 서비스<br>좌석 예매, 결제, 재고 관리처럼 안정성이 중요한 경우 |

### 클라이언트 도구

| 방식               | I/O 모델              | 장점                                          | 단점                                                               |
| ---------------- | ------------------- |---------------------------------------------|------------------------------------------------------------------|
| **RestTemplate** | Blocking            | 코드가 단순하고 사용법이 직관적이라 초기에 빠르게 적용 가능           | Spring 6 이후로는 유지보수만 되고 있어, 새로운 기능이 거의 추가되지 않음. 높은 동시성 환경에서 성능 저하 |
| **WebClient**    | Non-blocking (리액티브) | 한 번에 많은 요청을 처리할 수 있어서 성능이 좋고, 비동기 처리에 강함    | 리액티브 개념을 알아야 해서 배우기 어렵고, 디버깅이 복잡함                                |
| **FeignClient**  | Blocking (기본)       | 인터페이스 기반(어노테이션)이라 코드가 깔끔하고, 외부 API 연동 시 편리함 | 내부적으로 리플렉션을 써서 성능이 약간 떨어지고, 설정이 복잡할 수 있음. 복잡한 요청 처리에는 한계         |
| **RestClient**   | Blocking            | 코드가 간결하고 테스트가 쉬움                            | 아직 자료가 많지 않아 커스터마이징이 어렵고, 완전히 안정화된 단계는 아님                        |

#### Blocking vs Non-blocking
`Blocking`: 요청을 보낸 스레드가 응답이 올 때까지 기다림

→ 스레드가 대기 상태로 묶여 있어, 다른 일을 처리할 수 없음.

`Non-Blocking`: 요청을 보낸 스레드가 응답을 기다리지 않고 즉시 반환

→ I/O 작업이 끝나면 콜백/이벤트로 결과를 전달받음.


*동기/비동기는 논리적인 순서, blocking/non blocking은 물리적 실행(스레드) 관점

#### RestTemplate와 webClient의 동작 방식 비교

`RestTemplate`:

<img width="707" height="357" alt="Image" src="https://github.com/user-attachments/assets/1f55bd97-9f9b-476c-b22a-48274c167c27" />

RestTemplate는 Multi-thread, Synchronous Blocking.

Thread pool은 어플리케이션 구동시 미리 Thread를 만들어 놓음

Request가 오면 먼저 Queue에 쌓이고 비어있는 스레드가 있으면 . 스레드에 할당

각 스레드는 Blocking 방식으로 처리되어 응답이 올 때까지 그 스레드는 다른 요청에 할당될 수 없다.

-> 스레드가 다 찰 경우 queue에서 대기하는 시간 증가, 병목 현상 발생(성능 저하)

`WebClient`:

<img width="675" height="476" alt="Image" src="https://github.com/user-attachments/assets/be3e79c3-4616-4686-a7ce-814760aad31d" />

WebClient는 Asynchronous Non-blocking

각 요청은 Event loop 내의 job로 등록됨

event loop는 각 job를 제공자에게 요청하고 결과를 기다리지 않고 다른 job를 처리됨

event loop는 제공자로부터 callback로 응답이 오면 그 결과를 요청자에게 전달됨

-> 한 스레드가 병렬로 요청을 처리하여 RestTemplate보다 성능이 좋음.

#### 무엇을 쓸까?
다수의 결제 요청을 병렬적으로 처리하면서 성능을 높일 수 있는 WebClient를 이용하는 것이 이상적!

-> 그러나.. 이용하지 않았습니다

lock을 걸었음에도 WebClient의 비동기로 인한 트랜잭션 처리의 변수가 있을 수 있고, 시간이 없어서 직관적으로 코드를 짤 수 있던 RestClient 이용(나중에 변경할게요 ㅠ)


### 로깅 전략

| 레벨        | 목적                            |
| --------- | ----------------------------- |
| **ERROR** | 예외 발생, 시스템 장애 등 즉시 대응이 필요한 상황 |
| **WARN**  | 비정상 동작이지만 복구 가능한 상태           |
| **INFO**  | 정상 비즈니스 흐름 및 주요 이벤트           |
| **DEBUG** | 내부 상태 추적용 상세 로그 (개발용)         |

개발(dev): 콘솔 중심(SQL 쿼리는 제외), DEBUG 레벨까지.

운영(prod): 파일 중심, INFO 레벨까지. 