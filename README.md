# 2주차

## ERD

![ERD.png](image/ERD.png)

## 기능 구현

- 영화관 조회
- 영화 조회
- 영화 예매/취소

## @Entity

**회원**:

**예매**:

**영화관**:

**영화**:

**매점**:

+FK, 연관관계 매핑

FK: 외래 키

+연관관계

- 다대일(N:1)
- 일대다(1:N)
- 일대일(1:1)
- 다대다(N:N)

+연관관계 주인 → FK 관리하는 쪽

+값타입(Value Type)

- 임베디드 타입

+공통

- name
- price
- stockQuantity

+ENUM 타입

→ 속성으로 꼭 (EnumType.STRING) 주자! 기본값 (ORDINARY) → 숫자로 매핑됨

+cascade 속성

? 하이버네이트가 엔티티 영속화할 때.. 컬렉션 → 내장 컬렉션 만듦

컬렉션 넣어둔 다음에 persist 날릴 때

(cascade = CascadeType.ALL) 속성 달아두면

persist(필드명) → 하나로 컬렉션 다 persist 처리 할 수 있음

+Lombok(롬복)

생성자 → 어노테이션 관리

@Getter @Setter

읽기 메서드 제공 /

### +어노테이션

- 기본
    - @Id  → PK
        - @GeneratedValue
- 컬럼명 지정
    - @Column(name = “  “) → PK 테이블 컬럼명 지정
    - @JoinColumn(name = “ “) → FK 테이블 컬럼명 지정 (연관관계 주인 쪽에)
- 연관관계 매핑
    - @ManyToOne → 다대일
    - @OneToMany → 일대다

    - 연관관계 상하관계 표시: mappedBy = ‘field’) 속성으로 주인 지정 (field)

      → 주인이 아닌 쪽은 읽기만 가능

    - 지연 로딩 (fetch = FetchType.LAZY) 어노테이션에 달아주기
- 임베디드 타입
    - @Embeddable → 값 타입 정의하는 곳에 표시
    - @Embedded → 값 타입 사용하는 곳에 표시

## JPARepository

- 영속성 컨텍스트 → 1차 캐시

- 멀티 스레드

## Domain - Repository 설계

### 1. 회원 도메인

Member

MemberService

⬆️

MemberServiceImpl ➡️ MemberRepository

                                              ⬆️

H2MemberRepository / JPAMemberRepository

### 2. 조회 도메인

Cinema / Auditorium

Movie

### 3. 예매/취소 도메인

                                                                              회원 저장소 역할

                                                                                           |  회원 조회

클라이언트 ————————→ 예매 서비스 역할 ———————

                                                                                           | 가격 정책

                                                                            가격 정책 역할 + 할인…?

ReservaitonService

⬆️                                   ⬆️  ➡️  MemberRepository

⬆️                                   ⬆️                   ⬆️

⬆️                                   ⬆️       JpaMemberRepository

ReservaitonServiceImpl ➡️  ➡️  ➡️ 

                                                     ⬇️

⬇️ ➡️ CountPolicy

⬆️

TicketCountPolicy

CountPolicy → 가격 정책

- 영화관 종류 (특별관 / 일반관)
- 매수 (선택좌석수)
- 연령 (어른/어린이)

- 연관관계 주인 (Member- Reservation) : Reservation

  -예매 리스트 조회 서비스 EX)


```java
@Service
@RequiredArgsConstructor
public class ReservationQueryService {
  private final ReservationRepository reservationRepository;

  public Page<ReservationDto> getMemberReservations(Long memberId, int page, int size) {
    var pageable = PageRequest.of(page, size, Sort.by("reservedAt").descending());
    return reservationRepository.findByMemberId(memberId, pageable)
            .map(ReservationDto::from);
  }
}
```

DTO

정적 팩토리 메서드를 사용해서 DTO 사용

## Integration / Unit Test

Global Exception를 만들어봐요

[https://adjh54.tistory.com/79](https://adjh54.tistory.com/79)

Swagger 연동 후 Controller 통합 테스트

Service 계층의 단위 테스트






## JWT

인증에 필요한 정보들을 암호화시킨 토큰, Access Token으로 사용되며 Header, Payload, Verify Signature 객체가 필요하다!

JWT 구성 요소

1. Header(JSON) : Signature를 해싱하기 위한 알고리즘 정보들이 담겨 있음
2. Payload(JSON) : 서버와 클라이언트가 주고받는, 시스템에서 실제로 사용될 정보에 대한 내용들을 담고 있음
3. Verify Signature : 토큰의 유효성 검증을 위한 문자열

⇒ Header, Payload는 인코딩 O 암호화 X → SECRET KEY 없이 복호화할 수 없는 Verify Signature 필요

⇒ 해커가 토큰을 훔쳐 Payload 데이터를 임의로 조작해서 서버로 보내도 Verify SIgnature은 사용자의 정보를 기반으로 암호화되므로, 서버에서 Verify Signature을 검사하는 과정에서 유효하지 않은 토큰으로 간주한다.

JWT 장점

- 중앙의 인증서버, 데이터 스토어에 대한 의존성 없음, 시스템 수평 확장 유리
- Base64 URL Safe Encoding → URL, Cookie, Header 모두 사용 가능

JWT 단점

- Payload의 정보가 많아지면 네트워크 사용량 증가, 데이터 설계 고려 필요
- 토큰이 클라이언트에 저장되므로, 서버에서 클라이언트의 토큰을 조작할 수 없음

### JWT Security 설정

- JWT 관련
    - TokenProvider: 유저 정보로 JWT 토큰을 만들거나 토큰을 바탕으로 유저 정보를 가져옴
    - JwtFilter: Spring Request 앞단에 붙일 Custom Filter
- Spring Security 관
    - JwtSecurityConfig: JWT Filter를 추가
    - JwtAccessDeniedHandler: 접근 권한 없을 때 403 에러
    - JwtAuthenticationEntryPoint: 인증 정보 없을 때 401 에러
    - SecurityConfig: 스프링 시큐리티에 필요한 설정
    - SecurityUtil: SecurityContext에서 전역으로 유저 정보를 제공하는 유틸 클래스
    - CorsConfig: 서로 다른 Server 환경에서 자원을 공유에 필요한 설정

# 로그인 인증 4가지 방식 요약

## 1) 세션 + 쿠키(Session ID)

**인증 순서**

1. 사용자 로그인 시도
2. 서버에서 사용자 정보 확인 후, 서버 세션 저장소에 사용자 상태 저장 및 Session ID 발급
3. 서버 HTTP 응답 헤더에 발급된 Session ID를 보냄. 이후 매 요청마다 서버 HTTP 응답 헤더로 Session ID가 담긴 쿠키 전달
4. 서버가 쿠키를 받아 세션 저장소에서 대조 후 대응되는 정보 전달
5. 인증 완료 및 서버 → 사용자 데이터 전달

**장단점**

- (장) 쿠키엔 의미있는 사용자정보 없음(키만 보관), 서버가 강제 무효화 용이, 구현 단순.
- (단) 세션 저장소 필요(스케일링 부담), 쿠키 탈취 시 하이재킹 위험(HTTPS, SameSite, 짧은 만료 필요).

---

## 2) JWT Access Token만 사용

1. 사용자 로그인 시도
2. 서버에서 사용자 정보 확인 후, 사용자의 고유한 ID 값 부여 및 Payload에 정보 전달
3. JWT 토큰의 유효기간 설정
4. SECRET KEY를 통해 암호화된 Access Token을 HTTP 응답 헤더에 실어 보냄
5. 사용자는 Access Token을 받아 저장, 인증이 필요한 요청마다 토큰을 HTTP 요청 헤더에 실어 보냄
6. 서버에서는 해당 토큰의 Verify Signature를 SECRET KEY로 복호화, 조작 여부 혹은 유효 기간 검증
7. 검증 완료되면 Payload를 디코딩하여 사용자의 ID에 맞는 데이터 전달

**장단점**

- (장) 세션 저장소 불필요, 마이크로서비스/게이트웨이에 적합, 수평 확장 용이.
- (단) 발급 후 만료 전 **강제 무효화가 어려움**(유출 리스크), Payload는 디코딩 가능 → **민감정보 금지**.

---

## 3) JWT Access Token + Refresh Token(권장)

Access Token의 유효기간을 짧게 하면 로그인을 자주해야해서 번거롭고, 길게 하면 보안이 취약해지므로 이를 해결하고자 나온 것이 Refresh Token!

1. 사용자가 로그인 시도
2. 서버에서 회원 DB에서 값을 비교
3. 로그인 완료 시 Access Token, Refresh Token을 발급하여 HTTP 응답 헤더에 실어 보내고, 일반적으로 회원 DB에 Refresh Token을 저장
4. Refresh Token은 사용자의 DB에 저장 후, Access Token을 HTTP 요청 헤더에 실어 요청을 보냄
5. Access Token을 검증하여 이에 맞는 데이터를 보냄
6. Access Token 만료 시 사용자는 이전과 동일하게 Access Token을 HTTP 요청 헤더에 실어 보냄
7. 서버가 Access Token이 만료됨을 확인하고 권한 없음을 신호로 보냄
8. 사용자가 Refresh Token과 Access Token을 HTTP 요청 헤더에 실어 보냄
9. 서버는 받은 Access Token이 조작되지 않았는지 확인한 후, HTTP 요청 헤더의 Refresh Token과 사용자의 DB에 저장되어 있던 Refresh Token을 비교, Token이 동일하고 유효기간도 지나지 않았다면 새로운 Access Token을 발급함
10. 서버는 새로운 Access Token을 HTTP 응답 헤더에 실어 다시 API 요청을 진행함

**장단점**

- (장) 유출 피해 범위 축소(Access 짧음), 사실상 무상태에 가까운 운영 + 통제지점 확보.
- (단) 구현 복잡, 재발급/회전 로직, Refresh 저장소 운영 필요.

---

## 4) OAuth 2.0 (Authorization Code, 소셜 로그인)

구글/카카오 등 외부 권한서버에서 사용자 인증·토큰을 받아와 우리 서비스 계정과 연동.

**장단점**

- (장) 소셜 계정으로 빠른 온보딩, 외부 자원 접근 위임 표준.
- (단) 리다이렉트/코드 교환 등 플로우 복잡, 제공자별 설정·검증 필요.