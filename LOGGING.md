# 로깅 전략

<img width="1382" height="1042" alt="image" src="https://github.com/user-attachments/assets/f52fd949-4951-45d1-b20d-827993726794" />

| 예외 유형 | 설명 | 로그 레벨 |
| --- | --- | --- |
| **InvalidInputException** | 사용자가 잘못된 입력을 전달했을 때 | **WARN** |
| **ResourceNotFoundException** | 요청한 데이터가 없을 때, 정상 흐름에서 처리 가능 | **WARN** |
| **DuplicateRequestException** | 중복 요청 감지, 서비스는 계속 동작 | **WARN** |
| **DatabaseSaveException** | DB 저장 실패, 데이터 손상 가능 | **ERROR** |
| **ExternalApiException** | 외부 API 호출 실패, 기능 필수 | **ERROR** |

어떤 로그 레벨에서 어떤 로그를 남겨야할지?

- 공통
    - 요청 발생 시간
    - 요청 주체
    - 요청 서버
    - 요청 경로(엔드포인트)
- Error
  심각한 에러가 발생했거나, 애플리케이션의 정상적인 동작에 영향을 미칠 수 있는 문제가 발생했을 때
  ex) 사용자 user1의 /api/v1/orders 요청 중 NullPointerException 발생, 주문 처리 실패
- Warn
  애플리케이션의 정상적인 동작에 영향을 미칠 수 있는 문제

  ex) 사용자 user1의 /api/orders 요청 처리 시간 1500ms (기준 1000ms 초과)

- Info

  서비스가 정상적으로 동작하고 있으며, 운영/비즈니스 흐름상 중요한 이벤트가 발생했을 때

  ex) 사용자 user1가 /api/orders 요청을 POST로 실행, 응답 상태 200, 처리 시간 120ms