# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트

## 1️⃣ 

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

## 2️⃣~6️⃣ 

### swagger 캡처 화면
<img width="1312" height="926" alt="KakaoTalk_Photo_2025-09-21-오후 8-23-19" src="https://github.com/user-attachments/assets/f14a8d8d-2dd9-40dd-888f-a8e305dc8f1f" />
