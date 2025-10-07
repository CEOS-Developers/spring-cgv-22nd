# spring-cgv-22nd
CEOS 22기 백엔드 스터디 - CGV 클론 코딩 프로젝트


# DB 모델링
## 1. 요구사항 분석하기 & 엔티티 정의하기

---

### ❕CGV 클론코딩 프로젝트❕

**구현 기능**

1. 영화관 조회
2. 영화관 찜
3. 영화 조회
4. 영화 예매, 취소
5. 영화 찜
6. 매점 구매 (환불X)
- 기타 기능 설명
    - 모든 영화관에 특별관과 일반관이 존재해요
    - 특별관, 일반관 종류가 같다면 좌석은 동일해요
    - 좌석은 직사각형 형태로 존재해요 (중간에 비어있는 곳 없음, 통로 고려X)
    - 모든 매점 메뉴는 재고가 존재해요 (추후 과제를 위해 꼭 추가해주세요!)
    - 모든 영화관의 매점 메뉴는 같아요

---

![image (4).png](images/image%20(4).png)

- **회원 엔티티**
    - 회원 ID (PK)
    - 아이디
    - 비밀번호
    - 전화번호
    - 이메일
    - 닉네임
- **Base Entity**
    - 생성일
    - 수정일

  “모든 엔티티가 이를 상속받음 ⇒ 중복 코드 제거”

1. **영화관 조회 (CGV 찾기)**
2. **영화관 찜 (즐겨찾기 버튼)**

![image (3).png](images/image%20(3).png)

- **영화관 엔티티**
    - 영화관 ID (PK)
    - 이름
    - 지역
    - 주소
- **영화관_찜 엔티티**
    - 회원 ID (FK)
    - 영화관 ID (FK)
- **상영관 엔티티**
    - 상영관 ID (PK)
    - 영화관 ID (FK)
    - 상영관 이름
    - 상영관 종류(특별관/일반관)

3. **영화 조회(CGV 차트)**

4. **영화 찜 (즐겨찾기 버튼)**

![image (5).png](images/image%20(5).png)

- **영화 엔티티 (고정 정보)**
  - 영화 ID (PK)
  - 제목
  - 개봉일
  - 상영 시간
  - 장르
  - 감독
  - 영화 정보
  - 관람 등급
  - 예매율
  - 누적 관객 수
  - 에그 지수
- **~~영화_통계 (변하는 정보 - UPDATE 되는 정보 분리)~~**
  - ~~영화 ID (PK)~~
  - ~~예매율~~
  - ~~누적 관객 수~~
  - ~~에그 지수~~
- **영화_찜 엔티티**
    - 회원 ID (FK)
    - 영화 ID (FK)
- **상영_정보 (스케줄)**
    - 상영_정보 ID (PK)
    - 상영할 영화 ID (FK)
    - 상영할 상영관 ID (FK)
    - ~~상영 일시~~
    - 상영 시작 시간
    - ~~상영 종료 시간~~ (영화 테이블의 상영 시간 속성을 이용해서 계산)

5. **영화 예매, 취소**

- 특별관, 일반관 종류가 같다면 좌석은 동일해요
- 좌석은 직사각형 형태로 존재해요 (중간에 비어있는 곳 없음, 통로 고려X)

![image (6).png](images/image%20(6).png)

![image (7).png](images/image%20(7).png)

- **상영관_종류 엔티티 (좌석 엔티티와 분리해서 종류에 따라 한번만 저장)**
    - 상영관_종류 ID (PK)
    - 종류 이름
    - 좌석 총 행 수
    - 좌석 총 열 수
- **좌석 엔티티**
    - 좌석 ID (PK)
    - 상영관 종류 (FK)
    - 좌석 행
    - 좌석 번호
- **예약 엔티티**
    - 예약 ID (PK)
    - 회원 ID (FK)
    - 상영 정보 ID (FK)
    - ~~예매 일시~~ (BaseEntity의 생성 일시로 대체)
    - ~~예매한 좌석~~ (한 사람이 여러 좌석을 예매할 수 있기 때문에 예매된 좌석 테이블 필요)
    - 취소 일시
    - 상태 (BOOKED, CANCELED)
    - 결제 수단
    - 총 결제 금액
- **예매된 좌석 (추가)**
    - 예매 좌석 ID (PK)
    - 예약 ID (FK)
    - 좌석 ID (FK)

6. **매점 구매 (환불X)**

- 모든 매점 메뉴는 재고가 존재해요 (추후 과제를 위해 꼭 추가해주세요!)
- 모든 영화관의 매점 메뉴는 같아요

![image (8).png](images/image%20(8).png)

- **매점_상품 엔티티**
    - 상품 ID (PK)
    - 상품명
    - 가격
- **재고**
    - 재고 ID (PK)
    - 영화관 ID (FK)
    - 상품 ID (FK)
    - 수량
- **구매**
    - 구매 ID (PK)
    - 구매한 회원 ID (FK)
    - 구매한 영화관 ID (FK)
    - 구매 일시
- **구매 상세 (구매 - 매점상품의 중간 테이블, 여러 상품을 동시에 구매 가능)**
    - 구매 상세 ID (PK)
    - 구매 ID (FK)
    - 구매한 상품 ID (FK)
    - 구매 수량

<br>

### ~~Q. 영화 - 영화 통계 분리한 이유?~~

### ~~A. 성능 최적화를 위해~~

~~- **~~DB 잠금(Lock) 감소**: ‘예매율’처럼 매우 자주 바뀌는 정보와 ‘영화 제목’처럼 거의 바뀌지 않는 정보가 한 테이블에 있으면, UPDATE가 일어날 때마다 테이블에 잠금(Lock)이 걸려 다른 사용자가 영화 정보를 조회(SELECT)하는 속도가 느려질 수 있다.~~~~

<br>

---

## 2. 매핑 카디널리티 정의하기

### Identifying vs Non-Identifying 관계란?

**식별 관계 (Identifying Relationship)**

- 부모 테이블의 기본 키(PK)를 **자식 테이블의 기본 키(PK)의 일부로** 사용하는 관계
- 실선으로 표현

**비식별 관계 (Non-Identifying Relationship)**

- 부모 테이블의 기본 키(PK)를 **자식 테이블의 일반 외래 키(FK)로만** 사용하는 관계
- 점선으로 표현

⇒ 여기선 **Users-Cinema_likes, Cinemas-Cinema_likes, Users-Movie_likes, Movies-Movie_likes, Movies-Movie_stats** 만 **Identifying 관계**

<br>


| 관계 (1) | 관계 (N) | 관계 종류 | 설명 |
| --- | --- | --- | --- |
| Users | Cinema_Likes | 1:N	 | 한 회원은 여러 영화관을 찜할 수 있다. |
| Cinemas | Cinema_Likes | 1:N	 | 하나의 영화관은 여러 회원에게 찜을 받을 수 있다. |
| Users | Movie_Likes | 1:N	 | 한 명의 회원은 여러 영화를 찜할 수 있다. |
| Movies | Movie_Likes | 1:N	 | 하나의 영화는 여러 회원에게 찜을 받을 수 있다. |
| Users | Bookings | 1:N	 | 한 회원은 여러 번 예매할 수 있다. |
| Users | Purchases | 1:N | 한 회원은 여러 번 구매할 수 있다. |
| Cinemas | Screens | 1:N | 한 영화관은 여러 상영관을 가진다. |
| Cinemas | Inventories | 1:N | 한 영화관은 여러 상품의 재고를 가진다. |
| Screen_Types | Screens | 1:N | 한 종류의 상영관은 여러 곳에 있을 수 있다. |
| Screen_Types | Seats | 1:N | 한 종류의 상영관은 여러 좌석 정보를 가진다. |
| Movies | Movie_Stats | 1:1 | 한 영화는 하나의 통계 정보를 가진다.  |
| Movies | Showings | 1:N | 한 영화는 여러 번 상영될 수 있다. |
| Screens | Showings | 1:N | 한 상영관에서 여러 영화가 상영될 수 있다. |
| Showings | Bookings | 1:N | 한 상영 스케줄에 여러 예약이 있을 수 있다. |
| Bookings | Booked_Seats | 1:N | 한 예약에 여러 좌석이 포함될 수 있다. |
| Concession_Items | Inventories | 1:N | 한 상품은 여러 영화관에 재고가 있을 수 있다. |
| Concession_Items | Purchase_Details | 1:N | 한 상품은 여러 번 팔릴 수 있다. |
| Purchases | Purchase_Details | 1:N | 한 구매(영수증)에는 여러 상품이 포함될 수 있다. |


![Cgv-clone.png](images/Cgv-clone.png)
- `VARCHAR`는 **길이 제한이 있는 짧은 글**에, `TEXT`는 **길이 제한이 거의 없는 긴 글**에 사용
- `FLOAT`나 `DOUBLE` 같은 부동 소수점 타입은 0.1처럼 간단한 소수도 정확하게 표현하지 못하고 근사값으로 저장, `DECIMAL`은 값을 내부적으로 문자열(string)처럼 처리하여 저장하기 때문에 **계산 시 오차가 발생하지 않음**
<br>
---


# 프로젝트 코드 작성

## 프로젝트 구조

**: 계층형 구조 (Layered Architecture)**

```java
com
└── ceos22
    └── springcgv
        ├── controller
        ├── service
        ├── repository
        └── domain 
            ├── User.java
            └── Movie.java
```

### BaseEntity

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate // 엔티티 생성 시각을 자동 주입
    @Column(updatable = false) // 생성일은 수정되지 않도록 설정
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티 수정 시각을 자동 주입
    private LocalDateTime updatedAt;
}

```

- @MappedSuperclass
  - 이 클래스는 다른 엔티티에게 필드를 상속해주는 역할만 한다는 것을 명시
- @EntityListeners(AuditingEntityListener.class)
  - 생성일/수정일 자동 감지 기능 활성화
- @CreatedDate
  - 엔티티 생성 시각을 자동 주입
- @LastModifiedDate
  - 엔티티 수정 시각을 자동 주입

### SpringCgvApplication

```java
@SpringBootApplication
@EnableJpaAuditing
public class SpringCgvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCgvApplication.class, args);
	}

}

```

- @EnableJpaAuditing
  - JPA Auditing 기능 활성화 (Audit: 감시, 검증)
  - JPA가 엔티티의 생성, 수정 시점을 감지
  - `@CreatedDate`, `@LastModifiedDate`가 붙은 필드에 현재 시간을 자동으로 주입

### User

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(unique = true, nullable = false, length = 50)
    private String nickname;

}

```

- @Entity
  - JPA의 엔티티임을 나타냄
- @NoArgsConstructor(access = AccessLevel.PROTECTED)
  - 파라미터가 없는 기본 생성자를 자동으로 생성
  - 생성자의 접근 수준을 protected로 지정
- @Id
  - 해당 필드가 테이블의 기본 키(Primary Key)임을 나타냄
- @GeneratedValue(strategy = GenerationType.IDENTITY)
  - 기본 키의 값을 자동으로 생성
  - 엔티티의 PK 값을 데이터베이스가 자동으로 생성하도록 위임(AUTO_INCREMENT)

### Movie

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "movies")
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 100)
    private String director;

    private int runtime; // 분 단위

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_rating", nullable = false)
    private AgeRating ageRating;

    @Column(name = "booking_rate", precision = 4, scale = 1)
    private BigDecimal bookingRate;

    @Column(name = "cumulative_audience")
    private int cumulativeAudience;

    @Column(name = "egg_rate")
    private int eggRate;
}

```

### AgeRating

```java
public enum AgeRating {
    ALL, AGE7, AGE12, AGE15, AGE19
}
```

### MovieLike

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "movie_likes")
@IdClass(MovieLike.class)
public class MovieLike extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
```

- @IdClass(MovieLikeId.class)
  - 복합키를 위한 IdClass
  - IdClass로 MovieLikeId 클래스를 사용
  - 복합 기본 키 (user_id, movie_id)
- @ManyToOne(fetch = FetchType.LAZY)
  - N : 1에서 N에 해당하는 엔티티가 1에 해당하는 엔티티와 연관 관계를 매핑할 때 사용
  - 지연 로딩

### MovieLikeId

```java
@NoArgsConstructor
@EqualsAndHashCode
public class MovieLikeId implements Serializable {
    private Long user; 
    private Long movie; 
}
```

- MovieLike 엔티티의 @Id로 선언한 필드명과 동일한 이름과 타입이어야 함


### CinemaController

```java
@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("/cinemas")
    public ResponseEntity<List<CinemaDto>> getCinemasByRegion(@RequestParam String region) {
        List<CinemaDto> cinemas = cinemaService.findCinemasByRegion(region);
        return ResponseEntity.ok(cinemas);
    }
}
```

- 영화관 조회 API
- GET /cinemas
- `@RequestParam String region`: URL에 포함된 `?region=값` 형태의 쿼리 파라미터를 `region`이라는 문자열 변수에 담음
  - ex) http://localhost:8080/cinemas?region=서울

### CinemaRepository

```java
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    List<Cinema> findByRegion(String region);
}
```

- findAByB
  - 데이터를 SELECT하는 SQL 쿼리 자동 생성
- Region
  - Cinema 엔티티에 있는 region 필드를 검색 조건(WHERE)으로 사용


---

## 인증과 인가

### 인증 (Authentication)


: “누구인지 증명” → 로그인

### 인가 (Authorization)


: “접근 권한 확인하고 허가”

### 회사 비유

1. **인증 (Authentication) = 사원증 검사**
  - 출입 게이트에서 사원증을 태그
2. **인가 (Authorization) = 부서 접근 권한**
  - 5F 인사팀 / 7F 개발팀 / 10F 임원실

# 로그인 인증 방식

## 세션 기반 인증 (Stateful)

서버가 사용자의 로그인 상태 정보(세션)를 저장하고 관리하는 방식입니다.

- **동작 방식**
  1. 사용자가 로그인에 성공하면 서버는 **세션 저장소**에 사용자 정보를 저장하고, 그 저장소에 접근할 수 있는 특별한 **세션 ID**를 생성합니다.
  2. 서버는 이 세션 ID를 **쿠키**에 담아 사용자 브라우저에 보냅니다.
  3. 사용자는 이후 모든 요청에 이 세션 ID가 담긴 쿠키를 함께 보냅니다.
  4. 서버는 쿠키 속 세션 ID를 받아 세션 저장소와 대조하여 사용자를 식별합니다.
- **특징**: 서버에 사용자의 상태(State)가 저장되기 때문에 **Stateful**하다고 부릅니다.

## JWT 기반 인증 (Stateless)

서버가 사용자의 로그인 상태를 저장하지 않고, 요청에 포함된 토큰만으로 사용자를 식별하는 방식입니다. **JWT(JSON Web Token) 인증**이 가장 대표적인 예입니다.

- **동작 방식**
  1. 사용자가 로그인에 성공하면 서버는 사용자 정보와 권한, 만료 시간 등을 담은 암호화된 토큰(JWT)을 생성하여 사용자에게 전달합니다.
  2. 사용자는 이후 모든 요청의 헤더(Header)에 이 토큰을 담아 보냅니다.
  3. 서버는 토큰의 서명을 검증하여 데이터의 위변조 여부를 확인하고, 토큰 내부의 정보(Payload)를 통해 사용자를 식별합니다. 서버에 별도로 저장된 정보가 필요 없습니다.
- **특징**: 서버가 상태를 저장하지 않으므로 **Stateless**하다고 부릅니다. 확장성이 뛰어나고 모바일 앱 등 다양한 클라이언트 환경에 적용하기 좋습니다.

### 요약
| 구분 | **세션 기반 인증** | **토큰 기반 인증 (JWT)** |
| --- | --- | --- |
| **상태 저장** | 서버에 저장 (Stateful) | 서버에 저장 안 함 (Stateless) |
| **인증 정보** | 세션 ID (서버 저장소의 열쇠) | 토큰 자체 (필요 정보가 모두 담김) |
| **전달 매체** | 주로 쿠키 | 주로 HTTP 헤더 (Authorization) |
| **확장성** | 다소 불리 (서버 간 세션 공유 필요) | 유리 (토큰만 검증하면 됨) |


### Access Token (접근 토큰)

- **역할**: **실제 API 요청 권한을 증명**하는 단기 출입증입니다.
- **특징**:
  - **짧은 유효기간**: 보안을 위해 유효기간이 짧습니다. (예: 30분, 1시간)
  - **정보 포함**: 사용자 ID, 권한 등 필요한 정보를 담고 있습니다.
  - **탈취 위험**: 만약 이 토큰이 탈취되더라도, 유효기간이 짧아 피해를 최소화할 수 있습니다.
- **사용**: 사용자는 API를 요청할 때마다 이 Access Token을 헤더에 담아 보냅니다.

### Refresh Token (재발급 토큰)

- **역할**: **새로운 Access Token을 발급받기 위해** 사용하는 장기 출입증입니다.
- **특징**:
  - **긴 유효기간**: Access Token보다 훨씬 긴 유효기간을 가집니다. (예: 7일, 30일)
  - **보안된 저장**: 탈취되면 위험하므로, 서버의 안전한 DB나 클라이언트의 보안 스토리지에 저장됩니다.
  - **사용 최소화**: 실제 API 요청에는 사용되지 않고, 오직 Access Token이 만료되었을 때 **새 Access Token을 발급받는 용도**로만 사용됩니다.

### **동작 방식**

1. **최초 로그인**: 사용자가 로그인하면, 서버는 **Access Token**과 **Refresh Token**을 **모두** 발급합니다.
2. **API 요청**: 사용자는 **Access Token**을 사용하여 API를 요청합니다.
3. **Access Token 만료**: 시간이 지나 Access Token이 만료되면, API 요청이 거부됩니다.
4. **토큰 재발급**: 사용자는 가지고 있던 **Refresh Token**을 서버의 재발급 API로 보냅니다.
5. **재발급 성공**: 서버는 Refresh Token이 유효한지 확인하고, 새로운 **Access Token**을 발급해 줍니다. 사용자는 다시 로그인할 필요 없이 API 요청을 계속할 수 있습니다.

---

## 구현

### 의존성 추가

```java
	// Spring Security 0.12.16
	implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
	runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
	runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'
```

### SecurityConfig

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    //csrf disable
    http
            .csrf((auth) -> auth.disable());

    //Form 로그인 방식 disable
    http
            .formLogin((auth) -> auth.disable());

    //http basic 인증 방식 disable
    http
            .httpBasic((auth) -> auth.disable());

    http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/api/auth/signup", "/api/auth/login").permitAll()
                    .requestMatchers("/admin/**").hasRole("ROLE_ADMIN")
                    .anyRequest().authenticated()
            );

    //세션 설정
    http
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}

```
- `@EnableWebSecurity`
  - **SpringSecurityFilterChain 등록**: 웹 요청을 가로채 보안 처리를 담당하는 `springSecurityFilterChain`이라는 이름의 서블릿 필터(Servlet Filter)를 스프링 컨테이너에 등록
    - 서블릿 필터(Servlet Filter): 클라이언트의 요청(Request)이 서블릿(Servlet)에 도달하기 전과, 서블릿의 응답(Response)이 클라이언트에게 전달되기 전에 거치는 필터
    - 로그인 체크, 권한 검사, 요청/응답에 대한 로그 기록, Input/Output 데이터 가공, 인코딩 처리
  - 모든 요청에 대해 인증(Authentication) 요구
  - 폼 기반 로그인(Form-based Login) 및 HTTP Basic 인증 활성화
  - 세션 관리, CSRF(Cross-Site Request Forgery) 방어, 헤더 보안 설정 등
- **SpringSecurityFilterChain**
  - HTTP 요청 → 서블릿 컨테이너(WAS) → DelegatingFilterProxy →  **Security Filter Chain** (필터1 →  필터2 →  ...) →  DispatcherServlet →  컨트롤러
 <img width="1700" height="1014" alt="image" src="https://github.com/user-attachments/assets/5bb9c098-2314-4087-a304-38a36756c56b" />


- **DelegatingFilterProxy**
  - 서블릿 컨테이너가 관리하는 **DelegatingFilterProxy** 와 스프링 컨테이너가 관리하는 **Security Filter Chain** 연결


  - 동작 원리
    1. DelegatingFilterProxy 가 Servlet Container 로 넘어온 사용자의 요청을 받음
    2. DelegatingFilterProxy 는 SpringSecurityFilterChain 이름으로 생성된 Bean 을 ApplicationContext 에서 찾음
    3. Bean 을 찾으면 SpringSecurityFilterChain 으로 요청을 위임

  ### 기본 필터의 종류와 실행 순서

  스프링 시큐리티는 기본적으로 10개 이상의 필터를 체인에 등록

  1. `SecurityContextHolderFilter`
    - 모든 요청의 시작과 끝에서 `SecurityContext`를 생성하고 정리함
    - `SecurityContextHolder`에 대한 접근을 설정하는 매우 기본적인 필터
  2. `LogoutFilter`
    - 설정된 로그아웃 URL(기본값: `/logout`)로 오는 요청을 감시하고, 해당 요청이 오면 사용자를 로그아웃 처리함
  3. `CsrfFilter`
    - CSRF 토큰을 검증하여 세션 기반의 위조 공격을 방어. (JWT 방식에서는 보통 비활성화)
  4. `UsernamePasswordAuthenticationFilter`
    - 설정된 로그인 URL(기본값: `/login`)로 오는 아이디/비밀번호 요청을 처리하여 사용자를 인증
  5. `BasicAuthenticationFilter`
    - HTTP Basic 인증 헤더가 있는지 확인하고, 있다면 인증을 처리
  6. `ExceptionTranslationFilter`
    - 필터 체인에서 발생하는 `AuthenticationException`(인증 실패)이나 `AccessDeniedException`(인가 실패)을 감지하고 처리 예를 들어, 인증되지 않은 사용자라면 로그인 페이지로 보내거나 401 오류를 반환
  7. `AuthorizationFilter`
    - `SecurityConfig`에 설정된 `authorizeHttpRequests` 규칙(`hasRole`, `permitAll` 등)을 기반으로, 사용자가 해당 요청에 접근할 권한이 있는지 최종적으로 확인(인가)

- `BCryptPasswordEncoder`
  - Spring Security가 제공하는 BCrypt 해싱 알고리즘을 Bean으로 등록

- `csrf.disable()`: 세션 기반 공격인 CSRF 공격 방어 기능을 disable.
- `formLogin.disable()`: 폼(Form) 기반의 로그인 페이지 관련 기능 disable.
- `httpBasic.disable()`: HTTP Basic 인증 방식 disable.

- `.requestMatchers("/", "/api/auth/signup", "/api/auth/login").permitAll()`
  - `"/", "/api/auth/signup", "/api/auth/login"` 경로로 들어오는 요청은 누구나 접근할 수 있도록 허용함. (인증 불필요)
  - 회원가입/로그인은 인증 전 상태에서 접근해야 함

- `.sessionCreationPolicy(SessionCreationPolicy.STATELESS)`
  - 서버가 세션을 생성하거나 사용하지 않도록 설정 (Stateless 방식)
  

### JWTUtil - JWT 발급 및 검증 클래스

```java
@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}
```


- JWT 발급과 검증
  - 로그인 시
    - 로그인 성공 → JWT 발급
  - 접근 시
    - JWT 검증
- JWT를 암호화하고 서명하는 데 사용할 SecretKey를 생성하고 초기화
  - JWT 서명에 사용할 알고리즘을 HS256(HMAC SHA-256)으로 지정
  - secret 문자열은 application.yml 파일에 저장
- `Jwts.parser().verifyWith(secretKey).build()`
  - secretKey로 이 토큰의 서명이 유효한지 먼저 검증
  - 유효하다면 토큰에서 필요한 정보 추출
- `createJwt(String username, String role, Long expiredMs)`
  - username, role, 발행시간, 만료시간 정보를 payload에 담음
  - secretKey를 이용해 서명
  - header.payload.signature 형태의 압축된 JWT 문자열을 생성


### CustomUserDetails - UserDetails 구현체

```java
public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return user.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

- `User` 엔티티 객체를 스프링 시큐리티가 이해할 수 있는 `UserDetails` 타입으로 변환해주는 역할
- 스프링 시큐리티는 인증 과정에서 `UserDetails` 객체의 정보를 사용하여 비밀번호를 비교하고 권한을 확인함
- `public Collection<? extends GrantedAuthority> getAuthorities()`
  - 해당 유저가 가지고 있는 권한 목록을 반환
- `getPassword()`, `getUsername()`, `isAccountNonExpired()`
  - 인증 정보 제공
  - 계정 상태 정보 제공
  - UserDetails 인터페이스의 메서드 필수적으로  overriding 해야함

### CustomUserDetailsService - UserDetailsService 구현체

```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userData = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + username));

        return new CustomUserDetails(userData);
    }
}
```

- 스프링 시큐리티가 사용자 인증을 할 때 필요한 유저 정보를 DB에서 조회하는 클래스
- `loadUserByUsername(String username)`
  - 스프링 시큐리티가 로그인을 시도하는 유저의 `username` 을 이 메서드로 전달
  - DB에서 유저 조회
  - `User` 엔티티 객체를 이전에 만들었던 `CustomUserDetails` 객체로 변환해서 반환

### LoginFilter - UsernamePasswordAuthenticationFilter 구현체

```java
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        // 로그인 요청 URL
        setFilterProcessesUrl("/api/auth/login");
    }

    // 로그인 요청 시 호출되어 인증을 시도하는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON을 LoginRequestDto 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            // 스프링 시큐리티에서 사용할 인증 토큰(UsernamePasswordAuthenticationToken) 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // AuthenticationManager에게 인증을 위임
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 인증 성공 시 호출되는 메서드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // CustomUserDetails를 추출
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        // 사용자 이름과 역할을 추출
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        long expireMs = 60 * 60 * 1000L; // 1시간
        String token = jwtUtil.createJwt(username, role, expireMs);

        response.addHeader("Authorization", "Bearer " + token); // Authorization: Bearer [토큰값]
        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
    }

    // 인증 실패 시 호출되는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
```

- 사용자의 로그인 요청(`POST /login`)을 직접 처리하여 인증을 수행하고, 성공 시 JWT를 발급
- Form Login Filter를 disable 시켰기 때문에 `UsernamePasswordAuthenticationFilter` 커스텀 구현 필요
- 필터 등록

### SecurityConfig

```java
    http
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

```

### JWTFilter

```java
package com.ceos22.springcgv.config.jwt;

import com.ceos22.springcgv.config.CustomUserDetails;
import com.ceos22.springcgv.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //user를 생성하여 값 set
        User user = User.builder()
                .username(username)
                .password(null) // 임시 비밀번호
                .role(User.Role.valueOf(role))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}

```

- 클라이언트가 로그인 이후에 보내는 모든 요청을 가로채서, 요청에 포함된 JWT가 유효한지 검증하는 클래스
- 요청의 `Authorization` 헤더를 확인
  - 헤더가 없거나, Bearer  로 시작하지 않으면 `doFilter(request, response)` 로 다음 체인으로 넘기고 메서드 종료
- 토큰 만료 여부 확인
- 토큰이 검증되면(JWTUtil), 토큰의 payload에 저장된 `username`과 `role` 정보를 꺼냄
- 임시 User 및 UserDetails 생성
  - 토큰에서 꺼낸 정보를 바탕으로 `UserDetails` 객체(`customUserDetails`) 생성
- `CustomUserDetails`를 사용하여 `Authentication` 객체 생성
- 이 객체를 `SecurityContextHolder`에 저장
  - `SecurityContextHolder` 에 저장된  `Authentication` 객체는 애플리케이션의 어디서든 현재 로그인한 사용자의 정보를 쉽게 꺼내 쓸 수 있음

### SecurityConfig

```java
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
```

- LoginFilter 앞에 JWTFilter 등록


