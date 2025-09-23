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


## ERD
<img width="972" height="653" alt="Image" src="https://github.com/user-attachments/assets/d9d5b112-f6b5-4f02-ab72-59f028b296c6" />

- ### Movie
  <img width="553" height="333" alt="Image" src="https://github.com/user-attachments/assets/6ec54307-f0d2-4887-bd5d-5e1a1b534de2" />

  Movie(제목, 장르 러닝타임 등 영화의 기본 정보) -> 리뷰, 포스터, 트레일러 등과 일대다 관계

- ### Theater

  <img width="895" height="571" alt="Image" src="https://github.com/user-attachments/assets/30426e7a-6bd7-48ea-a5fa-3b52799253be" />

  Theater(영화관) 1:N Screen(상영관) 1:N Showtime(상영 시간표)

  Screen(상영관) 1:N Seat(좌석) 1:N ReservedSeat(예매 좌석)


- ### Reservation

  <img width="503" height="302" alt="Image" src="https://github.com/user-attachments/assets/4e7c2334-620e-46f1-a876-c8858fe7d88b" />

  Showtime(상영 시간표) 1:N Ticket(예매)

  Ticket(예매) 1:N ReservedSeat(예매 좌석)

  Showtime(상영 시간표) 1:N ReservedSeat(예매 좌석)

- ### 매점

  <img width="520" height="291" alt="Image" src="https://github.com/user-attachments/assets/059647de-fe6a-456c-a9d2-b4c6d5fefa1d" />

  Theater(영화관) 1:N Order(주문) 1:N OrderItem(주문 상품) 1:N Product(상품)

  Product(상품)-Theater(영화관) 1:N Stock(재고)


- ### 명세

    | 기능                   | Method | Endpoint | Request                                                                                      | Response (예시) |
    |----------------------|--------|----------|----------------------------------------------------------------------------------------------|-----------------|
    | 영화 목록 조회             | `GET` | `/movies` | -                                                                                            | ```[ { "movieId": 1, "title": "범죄도시 5", "ageRating": "AGE_15", "bookingRate": 35.5, "releaseDate": "2025-08-01T00:00:00", "totalAudience": 1200000, "eggNum": 92.1, "posterUrl": "https://example.com/poster1.jpg" } ]``` |
    | 특정 영화 상세 조회          | `GET` | `/movies/{movieId}` | `movieId`                                                                                    | ```{ "movieId": 1, "title": "범죄도시 5", "ageRating": "AGE_15", "releaseDate": "2025-08-01T00:00:00", "runtime": 125, "genre": "Action", "bookingRate": 35.5, "totalAudience": 1200000, "eggNum": 92.1, "prologue": "마석도 형사의 귀환!", "persons": [], "posters": [ { "posterId": 1, "imageUrl": "https://example.com/poster1.jpg" } ], "events": [ { "eventId": 1, "title": "범죄도시 5 GV 이벤트", "description": "주연 배우 무대인사" } ], "reviews": [ { "reviewId": 1, "content": "재밌다!", "rating": 5, "username": "sangeun" } ], "trailers": [ { "reviewId": 1, "videoUrl": "https://youtube.com/trailer1", "description": "메인 예고편" } ] }``` |
    | 특정 영화가 상영하는 영화관 조회   | `GET` | `/movies/{movieId}/theaters` | `movieId`                                                                                    | ```[ { "id": 1, "name": "용산CGV", "address": "서울시 용산구 한강대로", "accessInfo": "1호선 용산역 연결", "parkingInfo": "지하 3층 주차장", "region": "서울" } ]``` |
    | 특정 영화-영화관의 상영 시간표 조회 | `GET` | `/movies/{movieId}/theaters/{theaterId}/showtimes` | `movieId`, `theaterId`                                                                       | ```[ { "showtimeId": 1, "startTime": "2025-09-22T10:00:00", "endTime": "2025-09-22T12:00:00", "screenName": "1관", "screenType": "IMAX", "totalSeats": 200 } ]``` |
    | 극장 목록 조회             | `GET` | `/theaters` | -                                                                                            | ```[ { "id": 1, "name": "용산CGV", "address": "서울시 용산구 한강대로", "accessInfo": "1호선 용산역 연결", "parkingInfo": "지하 3층 주차장", "region": "서울" } ]``` |
    | 극장 상세 조회             | `GET` | `/theaters/{id}` | `theaterId`                                                                                  | ```{ "id": 1, "name": "용산CGV", "address": "서울시 용산구 한강대로", "accessInfo": "1호선 용산역 연결", "parkingInfo": "지하 3층 주차장", "region": "서울" }``` |
    | 특정 상영 시간표 좌석 조회      | `GET` | `/showtimes/{showtimeId}/seats` | `showtimeId`                                                                                 | ```{ "showtimeId": 1, "screenName": "1관", "totalSeats": 200, "seats": [ { "seatId": 1, "row": "A", "col": "1", "status": "AVAILABLE" }, { "seatId": 2, "row": "A", "col": "2", "status": "AVAILABLE" } ] }``` |
    | 티켓 예매                | `POST` | `/tickets/reserve` | ```{ "showtimeId": 1, "userId": 1, "generalCount": 1, "youthCount": 0, "seatIds": ["3"] }``` | ```{ "ticketId": 5, "showtimeId": 1, "generalCount": 1, "youthCount": 0, "finalPrice": 12000, "reservedSeats": [ "A2" ] }``` |
    | 티켓 취소                | `DELETE` | `/tickets/cancel/{ticketId}` | `ticketId`                                                                                   | `"예매가 취소되었습니다."` |

- ### 예매 과정
  1. 영화 선택
     
     보고싶은 영화 선택(`/movies` or `/movies/{movieID}`)
  2. 영화관 선택
     
     선택한 영화를 상영하는 극장 조회 및 선택(`/movies/{movieId}/theaters`)
  3. 상영 시간표 조회(`/movies/{movieId}/theaters/{theaterId}/showtimes`)
  4. 좌석 조회
  
     예매 가능한 좌석 조회(`/showtimes/{showtimeId}/seats`)
  5. 좌석 예매

     청소년/성인 인원 수, 좌석 선택 후 예매(`/tickets/reserve`)
