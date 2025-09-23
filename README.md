<img width="1980" height="1370" alt="cgv_erd_0921" src="https://github.com/user-attachments/assets/57c8044d-7e8a-4d64-ab17-cf7066cf8bae" />

1.  **영화관**
    -   Region 1 : N Theater
    -   Theater 1 : N Screen
    -   Screen 1 : N Seat

2.  **영화**
    -   Movie 1 : 1 Director
    -   Movie N : N Actor (중간 테이블 사용)

3.  **예매**
    -   Schedule N : 1 Movie
    -   Schedule N : 1 Screen
    -   Reservation N : 1 Member
    -   Reservation N : 1 Schedule
    -   Reservation N : 1 Seat

4.  **매점**
    -   Theater 1 : N Store
    -   Store N : N Product (중간 테이블 Store_Stock 사용 → Store 별로 재고 관리)
    -   Combo_Product_Association (콤보 메뉴에 포함되는 단일 메뉴 관리)

5.  **매점 주문**
    -   Order 1 : N Order_Detail

- Swagger 예매 테스트 결과
```java
{
  "success": true,
  "code": "S004",
  "message": "성공적으로 생성되었습니다.",
  "data": [
    {
      "reservationId": 1,
      "movieTitle": "F1 더 무비",
      "posterUrl": "string",
      "theaterName": "CGV 강남",
      "screenName": "1관",
      "screenType": "SCREENX",
      "startTime": "2025-09-21T12:00:00.734",
      "endTime": "2025-09-21T13:00:00.734",
      "rowName": "B",
      "colNumber": "2",
      "status": "CONFIRMED"
    },
    {
      "reservationId": 2,
      "movieTitle": "F1 더 무비",
      "posterUrl": "string",
      "theaterName": "CGV 강남",
      "screenName": "1관",
      "screenType": "SCREENX",
      "startTime": "2025-09-21T12:00:00.734",
      "endTime": "2025-09-21T13:00:00.734",
      "rowName": "B",
      "colNumber": "3",
      "status": "CONFIRMED"
    }
  ]
```

개선 사항

- 데이터베이스 인덱싱
    1. 지금은 한 사용자가 여러개의 좌석을 예매했을 때 예매 내역을 조회하기 위해 Reservation 테이블의 모든 행을 스캔해야 함.
        
        → 인덱싱을 통해 Schedule ID, Member ID 만으로 여러개의 예매 내역을 조회할 수 있음
        
        +) 특정 스케쥴의 예매 가능한 좌석 정보도 마찬가지
        
    2. 영화관 찜과 영화 찜이 하나의 테이블로 되어 있음
        
        → 두개를 분리
     3. 여러개의 좌석을 동시에 예매할 때 예매 가능한 좌석마다 save()를 하고 있음
         -> 하나의 예매에 대해서 여러번의 DB 접근이 발생하고 동시성 문제가 발생함
        -> 모든 좌석의 예매 가능 여부를 확인하고 한번에 save()하는 방식으로 개선
 4. 한번에 여러개의 좌석을 예매했을 때 가격정보, 예매 번호 등을 관리할 때 불리함
    -> 예매와 예매 좌석을 분리하여 관리
