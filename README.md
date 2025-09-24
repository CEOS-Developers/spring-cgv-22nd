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
