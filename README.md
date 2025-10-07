# 제목 없음

## Spring Security

- Spring Security : 웹 애플리케이션의 인증+인가를 책임지는 프레임워크
    - 인증 : 사용자 식별
    - 인가 : 사용자 권한 검증, 접근 제어

# Spring Security의 전체 흐름

- 클라이언트 요청
- Filter Chain 진입
    - 로그인 / 토큰 확인 등 필요한 필터를 거침
    - 모든 필터를 순서대로 성공적으로 통과한 요청은 DispatcherServlet으로 전달
    - 만약, 중간에 필터를 통과하지 못한다면 다음 필터로 넘기지 않고 에러 응답
- DispatcherServlet 도달
- 응답 반환

- 요청의 종류(URL)에 따라 필요한 필터들을 선택하여 통과하도록 할 수 있음
    - 특정 URL만 이 필터를 통과하도록 함
    - 특정 URL에서는 이 필터를 거치지 않도록 함
<img width="1506" height="745" alt="image" src="https://github.com/user-attachments/assets/f6f9ad97-6e47-4a74-a3f4-8aac7a71794a" />


→ 인증 과정에서 발생하는 전체적인 흐름

## 초기화

<img width="672" height="212" alt="image 1" src="https://github.com/user-attachments/assets/d7423914-6388-444e-a6da-e50ef4172091" />


WebSecurityConfiguerAdapter를 구현한 설정파일의 내용을 기반으로

HttpSecurity가 실제 필터를 생성합니다

각 설정 파일(WebSecurityConfiguerAdapter)별로 필터의 목록을 가지게 된 이후,

이 필터들이 WebSecurity으로 전달됩니다

WebSecurity는 각 설정 파일 별 목록을 전달받고, 이를 FilterChainProxy의 생성자 파라미터로 전달합니다.

결과적으로 FilterChainProxy는 Filter의 목록을 가지고 있게 됩니다

## 사용자 요청 이후
<img width="602" height="339" alt="image 2" src="https://github.com/user-attachments/assets/60b5dbe0-67b1-46c3-8363-21ba08f2b467" />


사용자 요청 이후 DelegatingFilterProxy가 가장 먼저 요청을 받아 FilterChainProxy에게 요청을 위임합니다.

DelegatingFilterProxy는 springSecurityFilterChain이라는 이름을 갖는 Bean을 찾는데,

FilterChainProxy가 바로 springSecurityFilterChain입니다.

(FilterChainProxy는 초기화 과정에서 Bean으로 등록되며, Filter 목록을 가지고 있습니다.)

FilterChainProxy는 위임받은 요청을 등록된 Filter들을 차례대로 거치게 합니다.

각 필터에서는 doFilter() 메서드에 정의된 작업을 수행하고, 다음 필터로 요청을 넘깁니다.

## FilterChain
<img width="1044" height="317" alt="image 3" src="https://github.com/user-attachments/assets/387e9f81-613a-4c10-aaf5-10bcc75592ee" />



---

### SecurityContextPersistenceFilter

세션 방식의 인증에서는 `SecurityContextRepository` 를 통해 이전에 생성된 SecurityContext를 불러오거나, 새로운 사용자에게 새로운 SecurityContext를 생성하고 SecurityContext를 영속화하는 역할을 합니다.

그럼에도 JWT를 사용하는 인증 방식에서도 SecurityContextPersistenceFilter를 사용하는데요,
그 이유는 SecurityContext를 사용하기 위해서입니다.

인증/인가를 처리하는 Security단과 로직을 처리하는 Service단은 분리되어 있어 사용자 인증/인가 정보를 
Service단에서 사용하기에 어려움이 있습니다.
이를 해결하기 위해 매 요청마다 사용자 정보를 Authentication에 저장하고, 이를 SecurityContext에 담아 영속화합니다. 영속화 된 데이터는 서버의 특정 메모리에 저장되어 Service단에서 접근하기 쉽습니다.

추가적으로 세션과 다르게 토큰 방식의 인증/인가에서는 요청이 끝날 때 마다
SecurityContext를 비영속화하며, 저장하지 않고 완전 삭제합니다.

---

### UsernamePasswordAuthentificationFilter

UsernamePasswordAuthentificationFilter는 사용자의 username, password으로 
사용자 인증을 처리하는 필터입니다.

1. 요청이 로그인에 대한 요청인지 URL을 확인합니다.
    - 로그인 요청이 아니면 다음 필터로 요청을 넘깁니다
2. attemptAuthentification() 메서드가 username, password를 추출하고 
인증 전의 Authentification 객체를 생성하여 AuthentificationManager에게 넘깁니다
3. AuthentificationManager는 전달받은 Authentification을 처리할 수 있는
AuthentificationProvider를 선택하고 요청을 위임합니다.
4. AuthentificationProvider는 UserDetailService를 통해 
DB에서 조회한 사용자 정보와 비교해 인증을 합니다.
-  DB에서 조회한 사용자 정보를 UserDetail으로 감싸서 반환합니다.
- PasswordEncoder으로 비밀번호를 검증합니다
5. 인증에 성공하면 Authentification을 SecurityContext에 저장하고
인증에 실패하면 AuthentificationException을 발생시킵니다.

+) UsernamePasswordAuthenticationFilter는 AbstractAuthenticationProcessingFilter를 상속받는다.

AbstractAuthenticationProcessingFilter는 사용자가 보낸 데이터의 형식에 따라 여러 구현체가 존재

- OAuth2LoginAuthentificationFilter : OAuth2 형식
- 이외에 JSON을 받도록 커스텀해서 구현하여 필터로 등록할 수도 있다.

---

### FilterSecurityInterceptor

인가처리를 담당하는 필터로 필터체인의 마지막에 위치하여
인증된 사용자의 권한을 확인하여 특정 URL에 대한 접근 권한을 확인합니다 
<img width="1204" height="554" alt="image 4" src="https://github.com/user-attachments/assets/b6d6d338-c788-4e52-ad47-8c4213600a51" />



1. 인증된 사용자인지 확인
2. SecurityMetadataSource는 사용자가 요청한 자원에 대한 접근 권한을 조회하여 권한 정보를
AccessDecisionManager에게 전달.
3. AccessDecisionManager 내부의 AccessDecisionVoter가 
사용자가 자원에 대한 권한이 있는지 투표를 받아 최종적으로 접근 허용 여부가 결정된다AccessDecisionVoter 는 아래의 3가지의 파라미터를 기준으로 투표를 한다.
    - Authentication - 인증 정보
    - FilterInvocation - 요청 정보
    - ConfigAttributes - 권한 정보

---

### **ExceptionTranslationFilter**

예외 처리를 담당하는 필터로, 
인증 및 인가 관련 예외를 적절히 처리하여 HTTP 응답 코드를 설정하거나, 
인증 실패 후의 리다이렉트 처리 등을 합니다.

---

여기까지 주요 필터들에 대해 살펴봤습니다.

이외에도 필요한 필터들을 가져와서 사용하거나, 
필요하다면 직접 구현하여 필터로 등록하여 사용하면 됩니다
