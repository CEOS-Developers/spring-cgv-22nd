# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트

## ERD
![img_1.png](img_erd.png)

### 1. MovieLike, CinemaLike
각 테이블마다 복합키로 `(member_id, movie_id)`, `(member_id, cinema_id)`를 PK를 잡게 해서 중복으로 찜을 하는 상황을 대비한다.

### 2. AuditoriumType
- 상영관의 세부 종류 (2D, IMAX 등)는 AuditoriumType으로 관리
- 일반관과 특별관의 구분은 is_special로 한다.

### 3. TypeSeat
- 같은 타입의 상영관이면 동일한 좌석 배치를 갖도록 분리한다.

### 4. Auditorium
- 해당 타입의 TypeSeat를 복제해 여러 개의 Seat를 생성한다.

### 5. Seat
- 좌석 타입 (일반석, 장애인석)을 분리한다.

### 6. BookingSeat
- `(screening_id, seat_id)`로 unique key 제약을 걸어, 데이터베이스 레벨에서 중복 예매 (한 회차에 같은 좌석을 여러 번 예매)를 차단하게 했다.

### 7. Product, Inventory
- 전 지점에서 메뉴는 같으나 재고는 지점별로 다를 수 있음을 고려했다.

### 8. ProductOrderItem 
- 주문 라인에서 수량 체크를 위해 분리했다.
- 분리한 이유는 하나의 주문(ProductOrder)에서 여러 품목(Product)을 담을 수 있게 했다.
- 또한 회원, 지점과 같은 주문 단위의 속성과 수량(품목) 같은 속성을 분리해 정규화를 하기 위해 별도로 테이블을 설계했다.

<hr />

[ 추가 내용 및 느낀점 ]
- TypeSeat를 하나의 템플릿처럼 사용하여 같은 타입의 상영관에서 반드시 동일하게 배치가 진행되게 하기 위해 별도 테이블을 설계하였다.
- 또한 일반석과 장애인석 등의 좌석별 속성을 지정하기 위해 Seat를 통해 유연한 확장을 하게 했다.

- 설계하면서 어디까지 정규화를 하는 게 맞고 어느 정도까지 성능을 고려해야 하는지 아직도 너무 헷갈린다. 공부 더 열심히 해야겠다..