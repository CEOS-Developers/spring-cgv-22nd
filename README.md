# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트

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