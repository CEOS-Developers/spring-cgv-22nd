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

<hr />

# Spring Security + JWT

- 로그인 (인증) : 로그인 요청을 받은 후 세션 방식은 서버 세션이 유저 정보를 저장하지만 JWT 방식은 토큰을 생성하여 응답한다.
- 경로 접근 (인가) : JWT Filter를 통해 요청의 헤더에서 JWT를 찾아 검증을하고 일시적 요청에 대한 Session을 생성한다. (생성된 세션은 요청이 끝나면 소멸된다)

## SecurityConfig 클래스
- 세션 기반 로그인 방식을 모두 disable 해줘야 한다.
- JWT를 통한 인증/인가를 위해서 세션을 stateless 상태로 설정하는 것이 중요하다.
- 또한 비밀번호를 암호화하여 저장하기 위해서 BCryptPaasswordEncoder를 등록해준다.

## 스프링 시큐리티 필터 동작 원리
- 스프링 시큐리티는 클라이언트의 요청이 여러개의 필터를 거쳐 DispatcherServlet(Controller)으로 향하는 중간 필터에서 요청을 가로챈 후 검증(인증/인가)을 진행한다.

### 클라이언트 요청 → 서블릿 필터 → 서블릿 (컨트롤러)
<img width="691" height="327" alt="스크린샷 2025-09-27 오후 10 32 57" src="https://github.com/user-attachments/assets/959a6a46-6d5a-493f-8f44-6ce0f2f9c35a" />


### Delegating Filter Proxy
- 서블릿 컨테이너 (톰캣)에 존재하는 필터 체인에 DelegatingFilter를 등록한 뒤 모든 요청을 가로챈다.
<img width="580" height="820" alt="image" src="https://github.com/user-attachments/assets/d55a5d93-00c3-4358-b053-b54cbb5cb0f3" />


- 서블릿 필터 체인의 DelegatingFilter → Security 필터 체인 (내부 처리 후) → 서블릿 필터 체인의 DelegatingFilter 
- 가로챈 요청은 SecurityFilterChain에서 처리 후 상황에 따른 거부, 리디렉션, 서블릿으로 요청 전달을 진행한다.
<img width="1250" height="777" alt="image" src="https://github.com/user-attachments/assets/c2ff7bc2-d93d-43d4-826b-a145c8aab07a" />


## JWT 생성 원리
JWT는 서버와 클라이언트 간에 정보를 안전하게 주고받을 수 있는 토큰 기반 인증 시스템. 이 토큰은 사용자 인증 정보와 기타 클레임(claim)을 포함하고 있으며, 서명되어 있어 위조가 불가능하다.

JWT는 Header.Payload.Signature 구조로 이루어져 있다. 각 요소는

- Header
    - JWT임을 명시
    - 사용된 암호화 알고리즘

- Payload
    - 정보

- Signature
    - 암호화알고리즘((BASE64(Header))+(BASE64(Payload)) + 암호화키)

기능들을 수행한다. JWT의 특징은 내부 정보를 단순 BASE64 방식으로 인코딩하기 때문에 외부에서 쉽게 디코딩 할 수 있다.  외부에서 열람해도 되는 정보를 담아야하며, 토큰 자체의 발급을 확인하기 위해서 사용한다.

https://www.jwt.io/

<hr />

# SecurityContextHolder

인증 정보 공유 방식

[Servlet Authentication Architecture :: Spring Security](https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html)

## **SecurityFilterChain 필터별 작업 상태 저장**

- **상태 저장 필요**

![](https://cafeptthumb-phinf.pstatic.net/MjAyNTA3MjhfMTg3/MDAxNzUzNjcyMzMyNzAz.jBTm0QdTjm31htsuT0WKAfF29hzhqwbUSe5_VZVhUYwg.HEiySfykNy_5nH1vLfS7oWKK8YS8OhT5ZzjL30VcAJgg.PNG/image-84459b21-ebd5-4b4a-a2ec-2919e4bb1d8c.png?type=w1600)

- SecurityFilterChain 내부에 존재하는 각각의 필터가 시큐리티 관련 작업을 진행한다. 모든 작업은 기능 단위로 분업하여 진행함으로 앞에서 한 작업을 뒤 필터가 알기 위한 저장소 개념이 필요하다.
- 예를 들어, 인가 필터가 작업을 하려면 유저의 ROLE 정보가 필요한데, 앞단의 필터에서 유저에게 ROLE값을 부여한 결과를 인가 필터까지 공유해야 확인할 수 있다.

- **저장 : Authentication 객체**
    
    ![image.png](attachment:2f5925bc-744b-47c5-a91c-dee071438cce:image.png)
    
    - 해당하는 정보는 Authentication이라는 객체에 담긴다. (이 객체에 아이디, 로그인 여부, ROLE 데이터가 담긴다.)
    
    - Authentication 객체는 SecurityContext에 포함되어 관리되며 SecurityContext는 0개 이상 존재할 수 있다. 그리고 이 N개의 SecurityContext는 하나의 SecurityContextHolder에 의해서 관리된다.
    
    - **Authentication 객체**
        - Principal : 유저에 대한 정보
        - Credentials : 증명 (비밀번호, 토큰)
        - Authorities : 유저의 권한(ROLE) 목록

<hr />

# 동시성 문제

- 멀티 스레드 환경에서 공유 리소스를 사용한다. 이는 context switching 시 오버헤드가 작아 메모리 리소스는 상대적으로 적으나, 자원 공유 시에 concurrency 이슈라는 단점이 있다. 즉, 여러 스레드가 동시에 하나의 자원을 공유하고 있기 때문에 같은 자원에 대해 race condition과 같은 문제가 발생하는 것이다.

동시성이란?
- 동시에 실행되는 것처럼 보이는 것

- 싱글 코어에서 멀티 스레드를 동작시키기 위한 방식으로, 멀티 태스킹을 위해 여러 개의 스레드가 번갈아가면서 실행되는 성질을 말한다.

- 멀티 스레드로 동시성을 만족시킬 수 있는 것이지 동시성과 멀티 스레드는 연관이 없다. 반례로 코틀린은 싱글스레드에서 코루틴을 이용하여 동시성을 만족할 수 있다.

- 코루틴(Coroutine): 싱글 스레드에서도 루틴(routine) 이라는 단위(맥락상 함수와 동일)로 루틴간 협력이 가능하며, 동시성 프로그래밍을 지원하고 비동기 처리를 쉽게 도와주는 개념을 말한다.

- 싱글 코어에서 멀티 스레드를 이용해 동시성을 구현하는 일부 케이스에 대한 내용이다. 멀티 코어에서 멀티 스레드를 이용하여 동시성을 만족할 경우에는 실제 물리적 시간으로 동시에 실행된다.

동시성 제어 방법
1. 암시적 lock (synchronized)
    - 문제가 되는 메서드/변수에 각각 synchronized 키워드를 넣는다.
      ```
      class Count {
          private int count;
          public synchronized int view() {return count++;}
      }

      class Count {
          private Integer count = 0;
          public int view() {
              synchronized (this.count) {
                  return count++;
              }
          }
      }
      ```
    - 배타적 실행
        - 한 스레드가 객체를 변경하는 중이라 상태가 일관되지 않은 순간의 객체를 다른 스레드가 보지 못하게 막는다.
        - 일관된 상태를 갖는 객체에 접근하는 메서드가 해당 객체에 lock을 걸어, 객체의 상태를 확인하고 수정한다.
            - synchronized로 설정된 메서드 혹은 코드 블럭에 lock이 생긴다.
        - 그 사이에 일관성이 깨지는 순간을 다른 어떤 메서드도 확인할 수 없다.

    - 스레드간 통신
        - 동기화된 메서드나 블록에 들어간 Thread가 같은 lock의 보호 하에 수행된 모든 이전 수정의 최종 결과를 보게 해준다.
        - 같은 임계 영역을 갖는 스레드에서 일관된 값을 보게 한다.
     
    
2. 명시적 lock
    - synchronized 키워드 없이 명시적으로 ReentrantLock을 사용하는 방법
        - synchronized 보다 세밀하게 락을 제어 가능하다. (Lock polling 지원 / 타임아웃 지정 가능 / condition을 적용해 대기 중인 스레드를 선별적으로 깨울 수 있음 / lock 획득을 위해 wating pool에 있는 스레드에게 인터럽트를 걸 수 있음)
        - 가장 중요한 것은 CPU cache와 메인 메모리 간의 동기화를 명시적으로 제어할 수 있게 된다는 것이다.
    - 해당 락의 범위를 메서드 내부에서 한정하기 어렵거나, 동시에 여러 락을 사용해야 할 때 쓴다.
    - 직접적으로 Lock 객체를 생성해 사용한다.
      ```
        public class CountingTest {
            public static void main(String[] args) {
                Count count = new Count();
                for (int i = 0; i < 100; i++) {
                    new Thread(){
                        public void run(){
                            for (int j = 0; j < 1000; j++) {
                                count.getLock().lock();
                                System.out.println(count.view());
                                count.getLock().unlock();
                            }
                        }
                    }.start();
                }
            }
        }
        class Count {
            private int count = 0;
            private Lock lock = new ReentrantLock();
            public int view() {
                    return count++;
            }
            public Lock getLock(){
                return lock;
            };
        }
      ```

3. 낙관적 lock
    - 앞서 나온 방법들은 동시성 문제를 나름대로 해결하지만, 다중화된 서버 환경에서는 보장할 수 없다는 단점이 있다. 동시성 제어를 위한 Lock 방법은 장치 내의 메모리 혹은 Lock을 통해 동기화 돼있기 때문에 이를 해결하기 위해서는 모든 서버의 동시성을 다룰 외부 시스템(DataBase, Redis 등)이 필요해진다.
    - 그래서 낙관적 락은 자원에 lock을 걸지 않고, 동시성 문제가 발생하면 그때 처리하는 방식이다.
    - 즉, DB의 Lock을 사용하지 않고, 애플리케이션 레벨에서 Entity의 버전을 관리하면서 변경을 감지하는 방법이다.
    - 하지만 경우에 따라 데드락에 걸릴 수 있기에 유의해야 하며, 애플리케이션 레벨에서 처리하는 특징으로 인해 재시도 로직을 별도 생성해야 한다.
    - 낙관적 락은 경합이 많고 충돌이 많을 수록 트랜잭션을 중단할 가능성이 매우 크고, 롤백은 테이블 행과 레코드를 모두 포함할 수 있는 현재의 보류 중인 변경 사항을 모두 되돌려야 하므로 DB 시스템 비용이 많이 들 수 있다.
  
4. 비관적 lock
    - 동시성 문제가 발생하기 전에 자원의 접근을 막아버리는 방식이다.
    - 즉, 동시성 문제가 발생할 것이라 가정하고 자원에 대한 접근을 막고 시작하는 것이다. 따라서 Transaction이 시작할 때 shared lock 또는 exclusive lock을 걸어버린다.


   1. shared lock (공유 락) : read lock이라고도 부르며 데이터를 읽을 때는 같은 shared lock끼리의 접근을 허용하지만, write 작업은 막는다.
   2. exclusive lock : write lock이라고도 부르며, 트랜잭션이 완료될 때까지 유지되면서 exclusive lock이 끝나기 전까지 read/write 작업을 모두 막는다.
       - 특정 데이터에 배타적 락(Exclusive Lock)을 걸어, 하나의 처리가 완료되기 전까진 해당 데이터의 읽기, 수정, 삭제를 방지하기 때문에 동시성 문제 측면에서 매우 안전하다.
       - 하지만 모든 작업을 순차적으로 처리하기 때문에 속도가 매우 저하되고, 특정 데이터의 조회까지 막기 때문에 전혀 상관없는 기능에서조차 병목 현상이 발생할 수 있다는 단점이 존재한다.


- 충돌이 많이 발생하는 환경에서는 낙관적 락보다는 비관적 락이 더 적합할 것이라 생각한다.


5. 분산 락
<img width="923" height="492" alt="image" src="https://github.com/user-attachments/assets/975a6011-a656-49ec-aa40-b7df24624a45" />

- 낙관적 락은 데이터의 쓰기 작업은 별로 없지만, 읽기 작업이 많아 동시 접근 성능이 중요할 때 많이 쓰이고, 비관적 락은 충돌이 많이 발생하더라도 데이터의 무결성을 지킬 수 있다는 장점을 갖는다.
- 하지만 충돌은 막으면서, DB의 부담도 줄이고 읽기 조회의 병목 현상도 줄일 수 있는 방법을 생각했을 때 락의 위치를 옮기는 것이 가장 쉽게 떠오르는 방법이다. DB보다 훨씬 접근이 빠른 Redis를 사용하면서, 특정 작업에서만 동시성이 관리되도록 처리하는 방법이다.


하지만 분산 락 개발 시에는 주의가 필요하다.
- 만약에 분산 락을 SpinLock 방식으로 구현했다고 하면 (-> busy waiting), 반드시 "락이 존재하는 지 확인한다"와 "존재하지 않으면 락을 획득한다"라는 두 연산이 atomic하게 이루어져야 하고 try 구문 안에서 Lock 획득에 성공할 때까지 무한 루프를 실행해야 한다.

이 방식에는 문제점이 있다.
1. lock timeout
   - 어떤 애플리케이션에서 tryLock에 성공해서 자원 접근을 막았는데 종료되어버리면, 다른 모든 애플리케이션도 영원히 Lock을 얻지 못하는 Dead Lock 현상이 발생한다.
  
2. tryLock은 try-finally 밖에서 수행해야 한다.
   - try 문에서 시도 횟수 초과에 대한 예외를 발생시키면, Lock을 얻지도 못 한 Thread에서 Lock을 해제시킬 수 있게 된다. 따라서 try-finally 밖에서 Lock 획득 시도 횟수 초과 예외를 처리해주어야 한다.

3. Redis 부하
   - SpinLock 방식은 Redis에 엄청난 부담을 줄 수밖에 없다. Critical Section 내에서 수행할 작업 속도가 느릴 수록 빈번히 수행되는 기능일 수록 문제는 더 심해진다.
  
일반적으로 많이 사용하는 Lettuce는 spin lock 기법으로 락을 획득하는 방식이다. 그럼 위와 같은 문제가 발생할 수 있기에 Redisson에 대해 찾아보았다.

[ Redisson ]
- Redis에 접근하는 다양한 인터페이스 중 하나다. 분산 락을 구현할 때 Redisson을 많이 사용하는 이유는 Pub/Sub 기능을 제공하기 때문이다.
- 이를 이용하면 락 획득에 실패하면 특정 key를 구독해두고 대기한다. 그러다가 lock을 얻을 수 있다는 이벤트가 발생하면 다시 lock을 선점하기 위해 시도한다.
    - Redisson도 SpinLock 방식을 지원은 하지만, 기본적으로 RedissonLock을 사용한다. 그리고 RedissonLock은 pubsub 기능을 사용함으로써 Redis에 가해지는 부하를 감소시킨다.
    - Lock을 획득하려고 시도했으나 실패하면, 해당 Lock에 대해 구독을 신청하고 대기 상태로 전환한다.
    - Critical Section에서 작업을 하던 Thread가 Lock을 해제하면, 구독 신청한 모든 Thread에게 알린다.
타임아웃이 지나면 Lock 획득에 실패하고 작업을 중단한다.

+) 이건 컬리 기술 블로그인데 분산 락에 대해 설명되어 있어서 참고해보시면 좋을 것 같습니다! https://helloworld.kurly.com/blog/distributed-redisson-lock/

해당 클론 코딩 프로젝트에서 동시성 문제를 고려할 부분을 2군데 (좌석 예매, 찜 기능)으로 생각했다.
1. 좋아요(찜) 기능
    - 여기서는 중복 insert 요청이 문제가 될 것일텐데, 락을 거는 것은 불필요하다고 생각했다.
    - db 레벨에서 제어가 될 것이라 판단

2. 좌석 예매 기능
    - 결제 서비스가 호출되면 좌석 선점과 결제가 하나의 트랜잭션으로 묶일 수 없기 때문에 Redisson을 이용한 분산 락 방식이 적절하다고 판단했다.
    - 결제 프로세스는 `좌석 선택 -> 임시 좌석 선점 -> 외부 결제 서비스 호출 -> 결제 성공 시 좌석 확정 (이때 DB insert) -> 결제 실패 및 취소 시 좌석 선점 해제` 로 생각했다.
    - 분산 락을 사용해도 DB unique 키 제약 조건을 유지해 혹시 모를 버그를 예방해서 데이터 무결성을 보장하도록 한다.

<hr />

# Feign Client
Feign Client란 Netflix에서 개발한 Http Client다. (HttpClient는 Http 요청을 간편하게 만들어서 보낼 수 있도록 돕는 객체정도로 생각)
처음에는 Netflix에서 자체적으로 개발을 진행했지만 현재는 오픈소스로 전환했으며 SpringCloud 프레임워크의 프로젝트 중 하나로 들어가있다.

- 장점
    - SpringMvc에서 제공되는 어노테이션을 그대로 사용할 수 있다. (Spring Cloud의 starter-openfeign을 사용할 경우)
    - RestTemplate 보다 간편하게 사용할 수 있으며 가독성이 좋다.
    - Feign Client를 사용한 통합 테스트가 비교적 간편하다.
    - 요청에 대한 커스텀이 간편하다. (요청이 실패했을때 몇초 간격으로 몇번 재요청을 보낼것인지를 구체적으로 정할 수 있다.)

- 단점
    - 동기적으로 동작한다. 즉, 하나의 요청이 끝나야 다음 동작이 가능하다.

장단점 위주로 정리하였고, 자세한 코드는 해당 기술 블로그 참고하면 좋을 것 같습니다. https://techblog.woowahan.com/2630/

<hr />

# 로깅 전략
- 자원 소모 방지를 위해 운영 환경 레벨은 INFO로 설정
- Logback 설정을 통해 JSON 형식으로 출력
- 민감 정보는 반드시 마스킹하기

1. ERROR
    - 즉각적 조치가 필요한 심각한 오류
2. WARN
    - 정상적이지 않은 상황이거나, 모니터링을 통해 원인 파악이 필요한 경우
3. INFO
    - 프로덕션 환경에서 사용할 기본 레벨
4. DEBUG
    - 개발 및 테스트 환경에서만 사용

<hr />

# 아키텍처 다이어그램
<img width="841" height="605" alt="ceos-22 drawio (1)" src="https://github.com/user-attachments/assets/fe58832f-55ca-4fd2-a7a7-8142749bc298" />

# 부하 테스트
- 테스트용 데이터 추가
<img width="650" height="468" alt="스크린샷 2025-11-08 오후 10 05 47" src="https://github.com/user-attachments/assets/572c4271-5fa2-4212-85db-b5cfe91ae846" />

- 모니터링 대시보드
<img width="1436" height="966" alt="스크린샷 2025-11-08 오후 10 33 53" src="https://github.com/user-attachments/assets/32cfcb4e-4a36-4eb8-97b7-e033abe092d1" />

<hr />

# 트랜잭션
- 트랜잭션은 시작 지점 & 종료 지점이 존재
- 시작 방법은 1가지지만, 끝나는 방법은 commit과 rollback 2가지

# 트랜잭션 전파 속성 (Transaction Propagation)
- **전파 속성** : 트랜잭션이 이미 진행 중일 떄 추가 트랜잭션 진행을 어떻게 할지 결정하는 것

## 물리 트랜잭션 / 논리 트랜잭션
- 트랜잭션은 커넥션 객체를 통해 처리하기 때문에, 1개의 트랜잭션을 사용하는 것은 하나의 커넥션 객체를 사용한다는 것
- 실제 데이터베이스의 트랜잭션을 사용한다는 점에서 -> 물리 트랜잭션이라고도 함
  - 물리 트랜잭션 : 실제 커넥션에 롤백/커밋을 호출하는 것. 즉 해당 트랜잭션이 끝나는 것

- 스프링의 입장에서는 트랜잭션 매니저를 통해 트랜잭션을 처리하는 곳이 2군데이기 때문에, 실제 데이터베이스 트랜잭션과 스프링이 처리하는 트랜잭션 영역을 구분하기 위해 스프링은 논리 트랜잭션이라는 개념을 추가함
- 예) 외부 트랜잭션과 내부 트랜잭션이 하나의 물리 트랜잭션(=커넥션)을 사용하는 경우
    - <img width="646" height="235" alt="스크린샷 2025-11-15 오후 10 05 20" src="https://github.com/user-attachments/assets/88706482-39e9-4942-93db-5a7bc5e2211f" />
    - 2개의 트랜잭션 범위 존재 -> 따라서 개별 논리 트랜잭션이 존재. 하지만 실제로는 1개의 물리 트랜잭션 사용

따라서 개념을 정리하면
- 물리 트랜잭션: 실제 데이터베이스에 적용되는 트랜잭션으로, 커넥션을 통해 커밋/롤백하는 단위
- 논리 트랜잭션: 스프링이 트랜잭션 매니저를 통해 트랜잭션을 처리하는 단위

- 기존의 트랜잭션이 진행중일 때 또 다른 트랜잭션이 사용되면 복잡해짐 => 스프링: 논리 트랜잭션이라는 개념을 도입
    - 원칙 1. 모든 논리 트랜잭션이 커밋되어야 물리 트랜잭션이 커밋됨
    - 원칙 2. 하나의 논리 트랜잭션이라도 롤백되면 물리 트랜잭션은 롤백됨
 
<br />

## 스프링의 트랜잭션 전파 속성

### 1. REQUIRED
<img width="631" height="230" alt="스크린샷 2025-11-15 오후 10 16 12" src="https://github.com/user-attachments/assets/cf184616-856d-432d-958a-798c3c8ac5ef" />
- 스프링이 제공하는 DEFAULT 전파 속성
- 기본적으로 2개의 논리 트랜잭션을 묶어 하나의 물리 트랜잭션을 사용하는 것 (이전 예시 사진)
- 내부 트랜잭션은 기존에 존재하는 외부 트랜잭션에 참여(이어 나감)하게 되며, 외부 트랜잭션의 범위가 내부 트랜잭션까지 확장됨. 따라서 내부 트랜잭션은 새로운 물리 트랜잭션을 사용하지 않음
- 커밋은 내부 1회, 외부 1회 총 2회 실행되고 물리 트랜잭션을 관리하는 외부 트랜잭션이 최종적으로 커밋될 때 실제로 커밋됨
- 롤백또한 내부 트랜잭션에서 롤백해도 즉시 롤백 X, 물리 트랜잭션이 롤백될 때 실제 롤백처리 됨.
    - 외부 트랜잭션의 롤백이 필요한 경우 : 내부 트랜잭션의 커밋/롤백 여부와 무관하게 물리 트랜잭션에서 롤백 시킴
    - 내부 트랜잭션에서 롤백이 필요한 경우(외부 t는 커밋 O) : 내부 트랜잭션에서 UnexpectedRollbackException 예외를 통해 롤백 필요 알림 -> 외부 트랜잭션에서 이를 바탕으로 롤백
 
### 2. REQUIRES_NEW
<img width="635" height="199" alt="스크린샷 2025-11-15 오후 10 16 33" src="https://github.com/user-attachments/assets/fecc864b-517a-4432-9cbb-2bea4feab819" />
- 외부/내부 트랜잭션을 완전히 분리하는 전파 속성
- 2개의 물리 트랜잭션 사용. 각각 트랜잭션 별로 커밋과 롤백이 수행
- 따라서 내부 트랜잭션 롤백이 외부 트랜잭션 롤백에 영향을 주지 않음
- 서로 다른 물리 트랜잭션 == 각각의 디비 커넥션 사용 (== 1개의 HTTP 요청에 대해 2개의 커넥션 사용)
    - 따라서 내부 트랜잭션 처리 중이 꺼내진 외부 트랜잭션은 대기 -> 커넥션 고갈 시킬 수 있기 때문에 주의
 
### 3. SUPPORTS
- 이미 시작된 트랜잭션이 있으면 참여 / 없으면 트랜잭션 없이 진행
- 트랜잭션이 없지만 해당 경계 내에서 커넥션이나 Hibernate Session 등을 공유 가능

### 4. MANDATORY
- REQUIRED와 유사. 이미 시작된 트랜잭션이 있으면 참여
- 하지만 트랜잭션이 없다면 생성하는 것이 아니라 예외를 발생
    - 따라서 혼자서 독립적으로 트랜잭션을 실행하면 안되는 경우에 사용
 
### 5. NOT_SUPPORTED
- 트랜잭션 사용 X
- 이미 진행 중인 트랜잭션이 있으면 보류시킴

### 6. NEVER
- 트랜잭션을 사용하지 않도록 강제
- 이미 진행 중인 트랜잭션도 존재하면 안됨. 있다면 예외 발생

### 7. NESTED
- 이미 진행 중인 트랜잭션이 있으면 중첩 트랜잭션 시작
    - 독립적인 트랜잭션을 만드는 REQUIRES_NEW와 다름
- 중첩된 트랜잭션은 먼저 시작된 부모 트랜잭션의 커밋/롤백에는 영향을 받지만, 자신의 커밋/롤백은 부모에 영향 X

<br />

<hr />

# 데이터베이스 인덱스
## 1. Clustered index
- 데이터베이스 테이블의 물리적인 순서를 인덱스의 키 값 순서대로 정렬하는 것
- 테이블 당 하나만 존재
    - 테이블의 데이터가 하나의 순서로만 정렬될 수 있기 때문
- 데이터의 물리적 순서와 인덱스 순서가 같음 -> 빠른 검색 가능
- 실제 데이터와 함께 저장되기 때문에 불필요한 디스크 공간을 추가적으로 유발하지 않음
- 기본적으로 MySQL 에서 InnoDB 스토리지 엔진을 사용하는 경우 테이블은 기본 키를 클러스터드 인덱스로 자동으로 사용

## 2. Non-Clustered Index
<img width="1710" height="638" alt="image" src="https://github.com/user-attachments/assets/e1bf2415-09d1-4f2d-a64d-fcb4ce2af2b2" />
- 데이터가 저장된 테이블과 별도의 공간에 위치. 데이터의 위치를 가리키는 포인터를 사용해 데이터를 찾음
    - 별도의 인덱스 테이블에 추가된 인덱스 값과 해당 데이터의 포인터가 저장됨
- 테이블의 데이터와 다른 순서로 정렬될 수 있음
- 인덱스가 테이블의 데이터와는 다른 별도의 저장 공간에 저장되며, 인덱스가 테이블 데이터와 다른 구조를 가짐
- 하나의 테이블에 대해 여러 개의 Non-Clustered Index 생성 가능
    - 따라서 다양한 검색 요구 사항에 맞춰 여러 인덱스 사용 가능
- Non-Clustered Index는 특정 컬럼에 대한 검색 최적화 가능
- PK 외에 다른 유니크 인덱스가 설정되는 경우, Non-Clustered Index로 unique index가 배치됨

## 3. B-Tree Index
<img width="963" height="253" alt="image" src="https://github.com/user-attachments/assets/e220e999-53ab-4bde-a88d-e8abe921edfb" />

- 모든 리프 노드가 같은 레벨이기 때문에 데이터베이스에서 검색/삽입/삭제 등의 동작이 일정 시간 안에 이뤄짐
- 키 값들이 정렬된 상태로 유지되어 빠르고 효율적인 검색이 가능
- 데이터 삽입/삭제 시 트리의 균형을 자동으로 맞추기 때문에 동적인 데이터 조정이 이뤄짐

## 4. Hash Index
<img width="315" height="230" alt="image" src="https://github.com/user-attachments/assets/b48cfd53-3322-4064-8a02-250ab71e4cfd" />
- 해시 테이블 기반
- 빠른 데이터 검색을 위해 사용되는 인덱스 유형 중 하나
- 특정 키 값을 해시 함수를 통해 해시 코드로 변환
- 하지만 DB 인덱스에서 해시 테이블이 사용되는 경우는 제한적
    - 해시가 등호(=) 연산에만 특화되었기 때문이며
    - 해시 함수는 값이 1이라도 달라지면 완전히 다른 해시 값을 생성하는데, 이러한 특성에 의해 부등호 연산(>, <)이 자주 사용되는 데이터베이스 검색을 위해서는 해시 테이블이 적합하지 않음
 
## 5. Bitmap Index
- 각각의 값에 대해 비트 배열 생성 (해당 값에 행 존재 시 : 1, 없으면 : 0)
- 예) 각 성별에 대해 비트 배열을 생성할 때, 성별 속성에 대한 남자 비트맵은 1 0 0 1 0
    - 따라서 해당 테이블 내에서 남자를 탐색하는 경우 비트맵 인덱스만 보고도 1, 4번째가 남자임을 알 수 있음
 
## 6. Full-Text Index
- 데이터베이스 내의 텍스트 데이터에서 키워드 검색을 가능하게 하는 인덱스 유형
- 대량의 텍스트 데이터에서 특정 단어나 문구를 빠르게 찾아내는 기능
- 주로 뉴스 기사, 책, 블로그 글 등의 텍스트 데이터를 검색할 때 사용
- 텍스트 데이터를 분석해 중요 데이터(키워드)를 추출하고, 이 단어들의 데이터베이스 내 위치를 기록하여 컴색 쿼리가 수행되는 경우 해당 인덱스를 사용해 빠르게 데이터를 검색함

### 장점
- 게시물 내용을 효율적으로 검색 가능
- 복잡한 쿼리 지원
    - 문구 검색, 불용어(별로 중요하지 않은 단어, 조사 등..을 후순위로 처리), 동의어 처리 등 복잡한 텍스트이 검색 요구사항 지원
### 단점
- 저장공간을 많이 사용함
- 텍스트 데이터가 자주 변경되면 업데이트 비용이 큼
- 대안이 많다!

## 7. Composite Index
- 두 개 이상의 컬럼을 결합해 생성한 인덱스
- 특정 쿼리 연산에서 여러 컬럼을 동시에 사용할 때 검색 성능을 향상시키기 위해 사용
- 지정된 컬럼들의 조합으로 구성되며, 인덱스 내에서는 해당 컬럼들이 정의된 순서대로 데이터를 정렬함

### 장점
- 여러 컬럼을 조건으로 하는 쿼리에서 검색 성능이 크게 향상
- 정렬 그룹화 최적화 가능
- 인덱스 커버링
    - 쿼리가 인덱스에 포함된 컬럼만을 사용하는 경우, 실제 데이터에 접근하지 않고 인덱스에서 결과를 바로 가져올 수 있음
### 단점
- 별도 공간, 유지관리 비용
