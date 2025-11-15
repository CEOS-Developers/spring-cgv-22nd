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

----
## 5주차 Docker 정리

### Docker의 등장 이유
- 과거에는 **"내 컴퓨터에서는 잘 되는데?"** 하는 문제가 잦았음
- 개발 환경에서는 잘 동작하던 애플리케이션이 운영 서버에 배포되면 오류 발생
- **원인:** 환경 차이
    - 운영체제 버전, 라이브러리, 설정 파일, 실행 경로 등이 시스템마다 다름
    - 같은 코드라도 결과가 달라질 수 있음
- 이를 해결하기 위해 **Docker**가 등장


### Docker의 핵심 아이디어
> **"애플리케이션과 실행환경을 하나로 묶자!"**

- 애플리케이션뿐만 아니라 필요한 **라이브러리, 설정까지 포함한 패키지 단위**로 묶음
- 이 단위를 **컨테이너(Container)** 라고 부름
- 컨테이너는 어디서 실행하든 **동일한 환경**과 **동일한 결과**를 보장
- “환경이 달라서 안 돌아간다”는 문제를 해결


### 컨테이너란?
- 애플리케이션 실행에 필요한 모든 요소를 포함한 **독립적 실행 환경**
- 코드, 런타임, 시스템 도구, 라이브러리 등 포함
- 하나의 시스템 안에서 여러 컨테이너를 동시에 실행 가능
- 각 컨테이너는 서로 **격리되어 독립적으로 작동**

### 가상머신(VM) vs Docker

| 구분 | VM | Docker |
|------|----|--------|
| **구조** | 하이퍼바이저 위에 Guest OS 실행 | 호스트 OS 커널을 공유 |
| **격리 수준** | OS 단위 격리 | 프로세스 단위 격리 |
| **속도 및 성능** | 부팅 느림, 무겁다 | 빠르고 가볍다 |
| **목적** | 여러 OS를 동시에 실행 | 앱을 빠르고 일관되게 실행 |

> VM은 완전한 시스템 격리가 필요할 때, Docker는 빠르고 유연한 배포가 필요할 때 사용

### Docker의 가장 큰 장점: **이식성(Portability)**
- 운영체제나 하드웨어에 구애받지 않고 동일한 환경에서 실행
- 개발·테스트·운영 환경이 항상 일관되므로  
  **"어디서 실행하든 같은 결과"** 를 보장


### Docker 주요 개념

#### Docker Daemon
- Docker의 엔진(서버 프로그램)
- 컨테이너를 **생성, 실행, 중지, 삭제, 관리**
- 백그라운드에서 항상 요청을 처리
- 이미지, 네트워크, 볼륨, 이벤트 등을 관리

#### 컨테이너
- 격리된 공간에서 프로세스로 동작
- 가상머신보다 가볍고, CPU/메모리를 효율적으로 사용
- 필요한 만큼의 리소스만 사용하여 성능 손실이 거의 없음

#### 이미지(Image)
- 컨테이너 실행에 필요한 **모든 설정과 파일의 스냅샷**
- 컨테이너는 이미지를 실행한 상태
- 동일한 이미지로 여러 컨테이너 생성 가능
- Docker Hub를 통해 공유 가능

#### Dockerfile
> 이미지를 자동으로 빌드하기 위한 설정 파일
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Docker가 유용한 상황
1. 애플리케이션 배포

    애플리케이션을 이미지로 패키징해 어디서든 동일하게 실행
    
    환경 설정이나 버전 문제로 인한 오류 감소

2. 테스트 및 개발

    여러 테스트 환경을 손쉽게 생성 가능
    
    CI/CD 파이프라인에서 일관된 테스트 환경 제공

3. 마이크로서비스 아키텍처

    각 서비스를 독립된 컨테이너로 분리
    
    서비스 간 충돌 없이 독립 실행 가능

4.  클라우드 환경

    AWS, GCP, Azure 등과 완벽하게 호환
    
    대규모 컨테이너 오케스트레이션(Kubernetes 등)과 함께 사용

5. 외존성(Dependency) 관리

    Node.js, Python, Java 등 실행 환경을 컨테이너에 포함
    
    버전 충돌, 환경 설정 문제를 최소화

## 배포 방식 정리

### 기본적인 배포 방식 (In-place Deployment)

> 이미 실행 중인 리소스(EC2, ECS 등)에서 기존 애플리케이션을 중지하고 새 버전으로 교체하는 방식

####  절차
1. 현재 실행 중인 애플리케이션 중지
2. 새 버전 배포 파일 업로드
3. 애플리케이션 교체
4. 서비스 재시작 및 상태 확인

#### 방법
- EC2 인스턴스에 직접 접속하거나 GitHub Actions 등을 이용해 새 버전 빌드
- AWS Elastic Beanstalk을 사용해 새 버전 배포 가능

#### 장점
- 개념이 간단하고 추가 리소스가 필요하지 않음

#### 단점
- 배포 중 **서비스 중단 시간(다운타임)** 발생
- 버전 롤백 시에도 서비스 중단이 발생

### 롤링 배포 (Rolling Update Deployment)

> 여러 개의 서버 인스턴스가 있을 때, **점진적으로 새 버전으로 교체**하는 무중단 배포 전략

#### 개요
- 여러 서버 인스턴스에 순차적으로 새 버전 배포
- 다운타임을 최소화하는 방식
- 점진적으로 새 버전으로 전환

#### 장점
- 무중단 배포 가능
- 특정 서버에만 문제가 생겨도 해당 서버만 롤백 가능
- 블루/그린 배포보다 빠른 전환

#### 단점
- 구버전과 신버전이 **동시에 실행될 수 있어 호환성 문제** 발생 가능
- 환경이 완전히 격리되지 않아 **롤백 시 복잡**할 수 있음

### 블루/그린 배포 (Blue/Green Deployment)

> 서로 다른 두 개의 동일한 환경(블루/그린)을 구성하고, 트래픽을 이동시켜 새로운 버전을 릴리스하는 방식

#### 개요
- **무중단 배포 방식**
- 블루 환경: 현재 버전
- 그린 환경: 새 버전
- 충분히 테스트 후 트래픽을 블루에서 그린으로 전환
- 블루/그린 중 하나만 프로덕션으로 지정

#### 장점
- 하나의 버전만 서비스 중이므로 관리가 용이
- 실제 서비스 환경에서 **새 버전 테스트 가능**
- 문제 발생 시 **즉시 구버전으로 롤백 가능**

#### 단점
- 두 환경(블루·그린)을 동시에 유지해야 하므로 **리소스가 2배 필요**


### 카나리 배포 (Canary Deployment)

> 트래픽의 일부만 새로운 버전에 전달해 점진적으로 전환하는 방식  
> **리스크를 최소화하고 안정성을 높이는 배포 전략**

#### 개요
- 기존 버전과 신규 버전을 모두 배포
- 일부 트래픽만 신규 버전에 전송 → 점차 전환 비율 확대
- A/B 테스트에 자주 사용

#### 장점
- 새로운 버전의 안정성을 점진적으로 확인 가능
- 사용자 영향 최소화
- 문제 발생 시 빠른 롤백 가능

#### 단점
- 트래픽 분할 관리 및 모니터링이 필요
- 배포 자동화 구성이 다소 복잡할 수 있음


### 요약 비교

| 방식 | 다운타임 | 리소스 사용 | 롤백 속도 | 특징 |
|------|-----------|--------------|------------|------|
| **In-place** | 있음 | 낮음 | 느림 | 단순하지만 중단 발생 |
| **Rolling** | 거의 없음 | 보통 | 중간 | 순차적 교체 |
| **Blue/Green** | 없음 | 높음 | 빠름 | 두 환경 간 완전 전환 |
| **Canary** | 없음 | 보통 | 빠름 | 트래픽 비율로 점진 배포 |

----
## 6주차 내용 정리

### CGV 서비스 아키텍쳐 구조도

<img width="840" height="368" alt="Image" src="https://github.com/user-attachments/assets/117b6d1a-35a6-4913-9abf-1c34a2ef592c" />

### 부하테스트(티켓 예매-결제 과정)

- 문제 상황 해결
    1. nginx connection
  
        <img width="985" height="361" alt="Image" src="https://github.com/user-attachments/assets/8bf9a888-29e6-48fc-aae5-14c4e9f91689" />
        
        Throughput이 0까지 떨어지는 문제 발생
  
        단순 병목(lock 등)으로 인한 문제는 아니라고 판단.
       
        -> 원인은 nginx connection 문제
  
        -> request 시 tcp 커넥션을 매번 새로 생성하는 과정에서 지연이 발생
  
        -> 이를 keepalive로 해결(tcp connection을 유지하고 재사용)

    2. 정적 팩토리 메서드 내 builder 패턴 사용
  
       <img width="728" height="436" alt="Image" src="https://github.com/user-attachments/assets/33c205ac-aa5f-470c-a7e7-a48426ac4d12" />
    
       <img width="641" height="184" alt="Image" src="https://github.com/user-attachments/assets/6230e2ed-d582-4a47-9597-0dc46543fc55" />
       
       HTTP Request failed가 100%고 post 요청이 실패하는 문제 발생
        
        -> 원인은 리팩토링 과정에서 사용한 정적 팩토리 메서드 내의 builder..(멍청 이슈..)
       
        -> 정적 팩토리 메서드 호출 -> builder -> builder 내에서 다시 정적 팩토리 메소드 호출 -> builder처럼 재귀 발생..


- 테스트 결과

    <img width="1482" height="714" alt="Image" src="https://github.com/user-attachments/assets/a8dcfbd2-55b5-4b61-b392-36c82a01c12e" />
    
    <img width="981" height="436" alt="Image" src="https://github.com/user-attachments/assets/35f676f5-e980-4a6c-a443-b1eddf44752f" />
    
    Throughput이 떨어지는 원인:
    
    <img width="843" height="184" alt="Image" src="https://github.com/user-attachments/assets/c0675234-60c2-4c53-a2e6-f3a7f8cce2ab" />
    
    <img width="831" height="187" alt="Image" src="https://github.com/user-attachments/assets/29bcb9dc-229a-4526-85c9-6dd6e16d5854" />
    
    application과 DB 병목이 원인이다! (그래프 모양이 이쁘지 않아서 제가 테스트를 제대로 했는지 의문이 드네요ㅠ)

    RedissonLockHelper로 인한 다중 락 경합, @Transactional 범위가 커서 락 + DB I/O가 오래 유지된다는 점 등이 원인이라고 추정됩니다.
    
* 로컬에서 시도하면 어떨까..??

    <img width="1480" height="780" alt="Image" src="https://github.com/user-attachments/assets/120ada17-ee0c-4c59-891d-04ff3eae8cf2" />
    
    <img width="980" height="701" alt="Image" src="https://github.com/user-attachments/assets/cef6b08b-f83e-443d-8050-c5729efb05b1" />
    
    Throughput이 떨어지긴 하지만, 서버와 달리 급격하게 떨어지는 모습은 보이지 않는다.
    
    원인: 서버와 달리 local은 nginx를 통하지 않아서 그런 것으로 추정.
    
    (그 외 cpu 환경의 차이도 있겠지만 가장 큰 원인은 이거 아닐까요..? 의견 남겨주시면 감사하겠습니다!)


구체적인 분석을 위해 예매 단계, 결제 단계의 부하를 각각 테스트 해보고 싶었으나, 결제를 위해서는 예매 단계에서의 payment record가 필요!

로직 분리를 시도하기 힘들어서 따로 테스트는 못해봤습니다.

----
## 7주차 내용 정리

### 트랜잭션 전파 속성

##### 1. REQUIRED

<img width="735" height="265" alt="Image" src="https://github.com/user-attachments/assets/4ff1d2ad-3ef6-4d20-9810-6e10ec6838c7" />

- 스프링이 제공하는 DEFAULT 전파 속성
- 기존 트랜잭션이 있으면 참가하고 없으면 새로운 트랜잭션 생성
- 내부(논리) 트랜잭션의 커밋은 외부(물리) 트랜잭션이 최종적으로 커밋될 때 실제로 커밋 (롤백도 마찬가지로 작동)

#### 2. REQUIRED_NEW

<img width="734" height="230" alt="Image" src="https://github.com/user-attachments/assets/cf11498c-6ac8-4f29-aed5-ef6955392ab4" />

- 항상 새로운 트랜잭션을 생성
- 기존 트랜잭션이 있다면 일시 중단하고 별도로 독립적으로 실행
- 부모 트랜잭션의 롤백 여부와 상관없이 이 트랜잭션은 따로 커밋/롤백됨
- 서로 다른 물리 트랜잭션을 별도로 가진다는 것은 각각의 디비 커넥션을 가진다는 것. 커넥션 고갈 발생할 수 있으니 조심히 사용

#### 3. SUPPORTS

- 기존 트랜잭션이 있으면 참가
- 없으면 트랜잭션 없이 실행
- 트랜잭션이 필수는 아니지만, 트랜잭션이 있으면 따라가도 되는 경우에 사용

#### 4. NOT_SUPPORTED

- 트랜잭션이 있으면 그 트랜잭션을 중단시키고 해당 메서드를 트랜잭션 없이 실행
- 읽기 전용 쿼리, 외부 API 호출 등 트랜잭션이 필요 없는 작업에 사용

#### 5. MANDATORY

- 반드시 기존 트랜잭션 안에서만 실행 가능
- 트랜잭션이 없으면 예외 발생

#### 6. NEVER

- 트랜잭션이 있으면 예외 발생
- 반드시 트랜잭션 없이 실행해야 할 경우 사용

#### 7. NESTED

- 기존 트랜잭션이 있으면 중첩 트랜잭션 생성
- 기존 트랜잭션이 없으면 새로운 트랜잭션 생성
- 중첩 트랜잭션은 부모 트랜잭션의 영향을 받지만, 중첩 트랜잭션이 외부에 영향을 주진 않음(중첩 트랜잭션이 롤백되어도 외부 트랜잭션은 커밋 가능)
- DB에서 SAVEPOINT 지원해야 동작
- 부분 롤백이 필요한 상황에서 사용

### 인덱스 종류

#### 1. B-Tree Index

<img width="720" height="385" alt="Image" src="https://github.com/user-attachments/assets/c7d36a9e-940f-4413-9d10-47c85fb44032" />

- 가장 일반적으로 사용되는 인덱스
- 균형 트리 구조로 검색/삽입/삭제 성능이 안정적
- 정렬된 상태를 유지하여 범위 조회에 강함(위 사진의 노란 화살표 확인)
- 대부분의 일반 컬럼에 적용하는 기본 인덱스

#### 2. Hash Index

<img width="689" height="474" alt="Image" src="https://github.com/user-attachments/assets/6cb02ca1-c3b0-4dc9-b9a8-8d059f8d6f92" />

- Hash 테이블 기반의 인덱스
- 동등 비교(=) 성능 최고
- 정렬/범위/LIKE 검색엔 불리함

#### 3. Unique Index

- 값의 중복을 허용하지 않는 인덱스
- 데이터 무결성 보장
- 검색 속도도 일반 인덱스보다 대체로 빠름(정렬된 상태 + 중복 없음)
- ex/ 이메일, 주민번호, 사업자 번호 같은 유니크한 값

#### 4. Composite Index

- 두 개 이상의 컬럼으로 만든 인덱스(ex/ (A, B, C))
- Leftmost Prefix Rule 적용(위 예시 컬럼에서 B만 검색하는 것은 불가, A 검색 혹은 A+B 검색)
- 멀티 컬럼 조건에 유리

#### 5. Full-Text Index

- 텍스트 문서 검색용 인덱스
- 문장의 단어 단위로 분리해 검색
- 게시물 내용을 효율적으로 검색 but 저장 공간 및 업데이트 비용 문제

#### 6. Clustered Index

- 데이터베이스 테이블의 물리적인 순서를 인덱스의 키 값 순서대로 정렬(PK)
- 테이블 당 하나만 존재(인덱스가 테이블의 일부로서 저장)
- 데이터의 물리적인 순서와 인덱스의 순서가 같아 빠른 검색 가능

#### 7. Non-Clustered Index

- 데이터가 저장된 테이블과 별도의 공간에 위치(하나의 테이블에 여러 개의 non-clustered 인덱스 생성 가능)
- 테이블의 데이터와 다른 순서로 정렬될 수 있음
- 데이터는 원본 테이블 위치를 참조
- Secondary Index라고도 부름

#### 8. Bitmap Index

- 컬럼의 각 값에 대해 비트 배열을 생성해 관리
- 특정 값이 존재하는 row 위치를 비트로 표시
- 여러 조건을 AND/OR 연산으로 비트 수준에서 빠르게 조합할 수 있음
- ex/ 값이 존재하면 1, 없으면 0이라고 가정, 성별 컬럼의 데이터가 [남, 여, 여, 남, 여]라고 할 때 "남"의 비트맵은 1 0 0 1 0. 비트맵 인덱스를 보고 1번,4번이 남자구나 알 수 있음

### 성능 최적화

#### 1. 중복 좌석 조회

```sql
EXPLAIN ANALYZE
SELECT *
FROM reservation_seat
WHERE showtime_id = 1
  AND seat_row = 'T'
  AND seat_col = '31'
  AND status = 'RESERVED';
```

- showtime_id를 인덱스로 사용

    <img width="930" height="85" alt="Image" src="https://github.com/user-attachments/assets/646f75a0-8e53-49cd-9a71-8da34caad958" />
    
    테스트 데이터의 수인 26000개의 row를 전부 다 읽음.(비효율)

- (showtime_id, row, col, status)를 인덱스로 사용

    <img width="930" height="77" alt="Image" src="https://github.com/user-attachments/assets/d9979329-2665-4f3b-be25-aece1bac72f3" />

    조건에 맞는 row를 하나만 읽음, 실행 시간도 0.0582ms로 매우 작음

#### 2. 결제 레코드 조회

결제 완료, 취소, 요청의 내역을 조회한다. (test 데이터 수: 25000)

```sql
EXPLAIN ANALYZE
SELECT *
FROM payment_record
WHERE type = 'TICKET'
  AND status = 'PAID'
ORDER BY created_at DESC
    LIMIT 50;
```

- 별도의 인덱스 설정 X

    <img width="932" height="84" alt="Image" src="https://github.com/user-attachments/assets/6a9199c2-2c9a-4340-8835-9407886a1447" />

    TICKET 조건으로 12,261행을 읽고 그 중에 PAID인 데이터 3047행을 읽음. 이후 3047행을 다시 정렬

- (type, status)를 인덱스로 사용

    <img width="927" height="80" alt="Image" src="https://github.com/user-attachments/assets/5bde5df8-e19b-41ea-acc6-f06f4dd4e7fb" />
    
    type, status에 해당하는 3037개의 행을 가져오고 created_at DESC 기준으로 정렬 수행, 최종적으로 6.6ms

- (type, status, created_at)을 인덱스로 사용

    <img width="929" height="76" alt="Image" src="https://github.com/user-attachments/assets/f34f5adc-c785-4736-9ba4-6f5e76ae63de" />
  
   총 실행 시간 0.75ms로 제일 빠름

#### 3. 영화관 조회

특정 지역의 영화관 조회 (test 데이터 수: 약 10000개)

```sql
EXPLAIN ANALYZE
SELECT *
FROM theater
WHERE region = 'SEOUL'
```

- 별도의 인덱스 설정 X

    <img width="687" height="83" alt="Image" src="https://github.com/user-attachments/assets/645fa598-8466-458f-b85f-1249e0b23ce2" />

    테이블 전체를 스캔 후 그 중 지역이 SEOUL에 해당하는 1113개의 행을 읽어옴(11.1ms)

- region을 인덱스로 사용

    <img width="717" height="55" alt="Image" src="https://github.com/user-attachments/assets/a41180d9-3400-45a6-9365-979e055bf819" />

  지역이 SEOUL에 해당하는 1133개의 행을 6.9ms의 실행 시간을 통해 가져옴
----