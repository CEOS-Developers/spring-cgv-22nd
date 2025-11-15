## 트랜잭션 전파 속성

: 이미 트랜잭션이 진행중일 때 추가 트랜잭션 진행을 어떻게 할지 결정하는 속성
전파 속성에 따라, 별도의 트랜잭션으로 진행할 수 도 있고, 기존 트랜잭션에 참여할 수 있다.
<img width="1280" height="448" alt="1" src="https://github.com/user-attachments/assets/8762464a-87a3-4dd1-9bd0-d7b9bdb4901f" />

---

## 물리 트랜잭션과 논리 트랜잭션

트랜잭션은 데이터베이스에서 제공하는 기술이므로, 커넥션 객체를 통해 처리합니다.
<img width="1280" height="387" alt="2" src="https://github.com/user-attachments/assets/4b8d24a0-bb40-434c-928b-139158c70c5a" />

1개의 트랜잭션을 사용한다는 것은 하나의 커넥션 객체를 사용한다는 것이고,
실제 데이터베이스의 트랜잭션을 사용한다는 점에서 이를 물리 트랜잭션이라고도 부릅니다.

정리하자면, 물리 트랜잭션은 종료 시점에 커밋/롤백하는 단위로, 데이터베이스 커넥션을 가지고 있습니다.

<img width="1280" height="449" alt="3" src="https://github.com/user-attachments/assets/645238b5-0344-4743-bf7e-46424317e8d0" />

새로운 트랜잭션이 기존의 트랜잭션에 참여할 경우, 스프링의 트랜잭션이 관리하는 트랜잭션은 2개이지만,
데이터베이스 커넥션은 1개입니다. 
논리 트랜잭션은 스프링이 각각의 트랜잭션의 영역을 구분하기 위해서 만들어진 개념입니다.

논리 트랜잭션은 종료 시점에 커밋 되지 않고, 소속된 물리 트랜잭션이 종료될 때 한번에 커밋/롤백 됩니다.

---

## 스프링의 트랜잭션 전파 속성

### REQUIRED

: 스프링의 디폴트 전파 속성, 기본적으로 2개의 논리 트랜잭션을 묶어 1개의 물리 트랜잭션을 사용합니다.
내부 트랜잭션은 기존의 외부 트랜잭션에 참가하게 됩니다.

1. **두개의 물리 트랜잭션이 모두 성공한 경우**

<img width="1195" height="651" alt="4" src="https://github.com/user-attachments/assets/667e8527-8701-4871-9a88-fbc9e8dd77a5" />

<img width="1195" height="653" alt="5" src="https://github.com/user-attachments/assets/12f62f01-de98-4909-94af-bf2bd7a9a701" />

1. **외부 트랜잭션에서 롤백을 호출한 경우**

<img width="1197" height="662" alt="6" src="https://github.com/user-attachments/assets/596e821c-90b0-47d5-8d48-ad0bcd398436" />

1. 내부 트랜잭션이 커밋을 요청 → 트랜잭션 매니저는 논리 트랜잭션임을 확인하고 실제 커밋을 하지 않음
2. 외부 트랜잭션이 롤백을 호출 → 물리 트랜잭션이므로 실제 롤백을 함
3. 물리 트랜잭션 종료

---

2. 내부 트랜잭션 롤백

<img width="1193" height="700" alt="7" src="https://github.com/user-attachments/assets/11c21c0b-8b59-4573-9333-d1047637b7f1" />

1. 내부 트랜잭션이 롤백을 호출함 → 논리 트랜잭션임을 확인하고 실제 롤백을 호출하지 않음
2. 트랜잭션 동기화 매니저에 rollBackOnly = true 으로 마킹함
3. 외부 트랜잭션이 커밋을 호출함 → 트랜잭션 동기화 매니저에 있는 마킹을 확인하고 실제 롤백을 호출함
4. UnexpectedRollbackException 예외를 던지고 물리 트랜잭션을 종료함

---

### REQUIRES_NEW

: 외부 트랜잭션과 내부 트랜잭션을 완전히 분리하는 전파 속성
각각의 트랜잭션은 별도의 커넥션을 가지고, 별도로 커밋/롤백을 수행합니다.

<img width="1280" height="387" alt="8" src="https://github.com/user-attachments/assets/8ad71272-4bf3-4edf-b196-89a2e790a2a0" />

하나의 요청에 대해서 여러개의 데이터베이스 커넥션을 사용하기 때문에
커넥션이 고갈되는 문제가 발생할 수 있어, 주의하여 사용해야한다.

---

### 이외의 전파 속성들

1. SUPPORTS
    - 기존 트랜잭션이 없으면 새로운 트랜잭션을 만듬
    - 기존 트랜잭션이 있으면 해당 트랜잭션에 참여함
2. **MANDATORY**
    - 기존 트랜잭션이 없으면 `IlegalTransactionStateException` 발생
    - 기존 트랜잭션이 존재하면 해당 트랜잭션에 참여함
3. **NOT_SUPPORTED**
    - 기존 트랜잭션이 없으면 트랜잭션 없이 진행
    - 기존 트랜잭션이 있으면 기존 트랜잭션을 보류시키고 트랜잭션 없이 진행
4. NEVER
    - 기존 트랜잭션 없으면 트랜잭션 없이 진행
    - 기존 트랜잭션이 있으면 `IllegalTransactionStateException` 발생
5. NESTED
    - 중첩 트랜잭션을 생성함
    - 중첩 트랜잭션은 부모 트랜잭션의 영향을 받는다
        - 부모가 롤백하면 롤백된다.
    - 부모 트랜잭션은 중첩 트랜잭션의 영향을 받지 않는다
        - 중첩 트랜잭션이 롤백된다고 해서 롤백되지 않는다.

---

## 인덱스 종류 조사

### **Clustered Index ( 클러스터드 인덱스 )**

<img width="1394" height="675" alt="9" src="https://github.com/user-attachments/assets/078a7fdd-a9ac-4e66-bbe5-cdff79de1e24" />

- 테이블 당 하나만 존재함
- 데이터의 순서와 인덱스의 순서가 같아서 빠른 조회가 가능하다
- Leaf Node에서 인덱스는 항상 정렬되어 있으며, 인덱스와 실제 데이터를 함께 저장함
    - 쉽게 말하면, 인덱스를 기준으로 실제 테이블이 정렬되어 있다고 생각하면 됨
- PK가 클러스터 인덱스로 설정된다.

장점 : 인덱스 기준으로 데이터가 함께 정렬되어 있어 검색 속도가 빠르다

단점 : INSERT / UPDATE / DELETE 마다 전체 데이터를 재정렬해야한다.

### Non-Clustered Index

<img width="1166" height="677" alt="10" src="https://github.com/user-attachments/assets/c72852fc-4502-44ae-8e3e-80b4b20d123a" />

- 인덱스 페이지를 실제 데이터와 별개의 저장공간에 저장함
- Leaf 노드는 RID(실제 데이터의 주소)를 가지고 해당 정보로 실제 데이터에 접근한다.
- 실제 데이터는 해당 인덱스로 정렬되어있지 않다
- 여러개의 Non-Clustered Index를 생성할 수 있다
- Unique 컬럼에 Non-Clustered Index가 구성된다

장점 : 실제 데이터는 정렬되지 않았기 때문에 INSERT / UPDATE / DELETE에 정렬 비용이 없다

단점 : 클러스터 인덱스에 비해 검색 속도가 느리다

정리하자면, 클러스터 인덱스는 인덱스를 통해서 리프 노드까지 탐색하여 바로 원하는 데이터를 찾을 수 있고
비 클러스터 인덱스는 인덱스를 통해 데이터의 위치를 받아, 실제 테이블에서 해당 데이터를 다시 찾아야한다

### B-Tree Index

<img width="1710" height="956" alt="11" src="https://github.com/user-attachments/assets/cc0395a0-6b92-4eb8-b40e-773381b80bfc" />

작동방식

- 검색
    1. 루트 노드에서 시작하여 키값을 비교해가며 원하는 값을 찾을 때 까지 내려간다.
    2. 이진 탐색을 통해 데이터를 찾는다
- 삽입
    1. 새로운 데이터를 삽입
    2. 노드가 가득차면 분할하여 균형을 유지한다.
    3. 트리 전체에 수정이 발생할 수 있다
- 삭제
    1. 삭제가 발생하면 각 노드 당 최소 키 개수를 유지하면서 노드를 병합하거나 재분배가 일어난다.

### Hash Index

<img width="1710" height="593" alt="12" src="https://github.com/user-attachments/assets/4d12d05a-4c28-4840-b3f9-eba8a0559b4f" />

작동방식

1. 해시 함수
    - 키 값에 대해 고정크기의 해시 코드를 출력하는 함수
    - 동일한 키 값에 대해 항상 같은 해시 코드를 반환
2. 해시 테이블
    - 해시 코드를 인덱스로 사용하여 데이터를 저장하는 테이블
    - 각 해시 코드는 테이블 내의 고유한 위치에 해당함
    - 해시 코드와 함께 저장된 실제 테이블의 주소로 해당 데이터에 접근함
3. 충돌 처리
    - 2개 이상의 키 값이 동일한 해시 코드를 가지는 경우 충돌이 발생한다.

3-1. 체이닝

<img width="1280" height="353" alt="13" src="https://github.com/user-attachments/assets/2a52b451-4a03-4e01-ad4d-e5a2359b3e39" />

동일한 해시 값을 갖는 여러개의 키 값을 Linked List으로 저장
연결 리스트로 인해 최악의 경우 O(N)의 수행시간이 될 수 있다.

→ 실제로는 균형 이진 트리로 저장하여 이 문제를 해결한다고 한다

3-2. 개방 주소법

<img width="1280" height="405" alt="14" src="https://github.com/user-attachments/assets/c2bae62d-26e3-4073-afc1-44130e4a2ab7" />

해당 인덱스에 이미 데이터가 존재하는 경우 해시 테이블의 비어있는 공간에 데이터를 삽입하는 방식

위의 사진에서는 가까이 있는 빈 공간 부터 중복 해시값을 갖는 데이터를 채우게 되는데
이렇게 되면 특정 해시 값에 데이터가 몰리게 되면 
해시 테이블의 특정 범위에 데이터가 집중되는 클러스터링이 발생할 수 있습니다.

이를 위해 이중 해싱이라는 방법을 사용합니다.

해시 값 중복이 발생했을 때 새로운 해시 함수를 한번 더 거친 해시 값의 위치에 저장하게 됩니다.

### 상황별로 좋은 인덱스 종류

- 해시 인덱스
    1. 단일 값에 대한 일치 검색이 많은 상황
    2. 범위 검색 & 정렬이 많이 없는 상황 
        
        → 해시 인덱스는 정렬되어있지 않기 때문에 범위, 부등호, 정렬을 지원하지 않는다
        
- B트리 인덱스
    1. 범위 검색 & 정렬된 결과 검색이 많은 상황
    2. 부등호 비교, 부분 일치 등의 검색이 필요한 상황

### 인덱스를 만들 컬럼을 결정

- 카디널리티가 높고, 활용도가 높은 컬럼이 인덱스를 설정하기 좋은 컬럼
- 규모가 큰 테이블
- INSERT, UPDATE, DELETE가 자주 발생하지 않는 컬럼
- WHERE, ORDER BY 절에 자주 사용되는 컬럼

### 커버링 인덱스

쿼리를 충족하는 데 필요한 모든 데이터를 인덱스의 컬럼들로만 구성하여
실제 데이터 테이블을 조회하지 않고 인덱스 테이블에서 바로 결과를 반환할 수 있는 인덱스를 의미함

즉, SELECT, WHERE, GROUP BY, ORDER BY 등 쿼리에 사용되는 모든 컬럼이 인덱스 내에 포함되어 있으면 커버링 인덱스입니다.

---

## 쿼리 성능 개선

```java
EXPLAIN ANALYZE
SELECT movie_id, title, running_time, release_date
FROM movie
WHERE status = 1 AND genre = 3;

'-> Filter: ((movie.genre = 3) and (movie.`status` = 1))  (cost=2047 rows=201) (actual time=0.406..11.4 rows=811 loops=1)\n    
-> Table scan on movie  (cost=2047 rows=20064) (actual time=0.389..9.97 rows=20000 loops=1)\n'

```

<img width="609" height="47" alt="15" src="https://github.com/user-attachments/assets/b10f5cee-2e08-4d97-b3a4-0988c9543d98" />

1. status에 인덱스 걸었을때

<img width="629" height="54" alt="16" src="https://github.com/user-attachments/assets/e070c538-c812-4af4-b2c7-02a855c6a3e3" />

```java
'-> Filter: (movie.genre = 3)  (cost=187 rows=666) (actual time=0.154..6.34 rows=811 loops=1)\n    
-> Index lookup on movie using idx_status (status=1)  (cost=187 rows=6656) (actual time=0.148..6.07 rows=6656 loops=1)\n'

```

1. genre에 인덱스 걸었을 때

<img width="656" height="74" alt="17" src="https://github.com/user-attachments/assets/1dc9af42-e4b6-4d56-a3d0-4021fb5828ed" />

```java
'-> Filter: (movie.`status` = 1)  (cost=146 rows=250) (actual time=0.383..3.41 rows=811 loops=1)\n    
-> Index lookup on movie using idx_genre (genre=3)  (cost=146 rows=2496) (actual time=0.377..3.27 rows=2496 loops=1)\n'

```

genre는 0~7 값을 가지고, status는 0~2의 값을 갖는다.

→ 카디널리티가 높은 속성을 선택하자

1. status, genre에 인덱스를 걸었을 때

<img width="668" height="55" alt="18" src="https://github.com/user-attachments/assets/f83a3e55-eb46-4e1e-8e22-4a2cddc58808" />

```java
'-> Index lookup on movie using idx_status (genre=3, status=1)  (cost=202 rows=811) (actual time=0.21..1.69 rows=811 loops=1)\n'

```

한번에 조건을 모두 만족하는 row만 탐색해서 빠르다 !

---

```java
EXPLAIN
SELECT movie_id, title, release_date, poster_url
FROM movie
WHERE status = 1
ORDER BY release_date DESC
LIMIT 20;
```

```java
'-> Limit: 20 row(s)  (cost=2047 rows=20) (actual time=9.97..9.97 rows=20 loops=1)\n    
-> Sort: movie.release_date DESC, limit input to 20 row(s) per chunk  (cost=2047 rows=20064) (actual time=9.97..9.97 rows=20 loops=1)\n        
-> Filter: (movie.`status` = 1)  (cost=2047 rows=20064) (actual time=0.0605..9.14 rows=6656 loops=1)\n            
-> Table scan on movie  (cost=2047 rows=20064) (actual time=0.0577..7.94 rows=20000 loops=1)\n'

```

1. status 인덱스

```java
'-> Limit: 20 row(s)  (cost=786 rows=20) (actual time=16.8..16.8 rows=20 loops=1)\n    
-> Sort: movie.release_date DESC, limit input to 20 row(s) per chunk  (cost=786 rows=6656) (actual time=16.8..16.8 rows=20 loops=1)\n        
-> Index lookup on movie using idx_status (status=1)  (cost=786 rows=6656) (actual time=0.217..15.4 rows=6656 loops=1)\n'

```

- 오히려 실행시간이 늘었다

→ (status = 1)이 전체 행수의 약 1/3에 해당하기 때문에 idx를 통해서 원본 데이터에 접근하는 오버헤드가 풀 테이블 스캔보다 큼

1. release_date 인덱스

```java
-> Limit: 20 row(s)  (cost=1.84 rows=2) (actual time=0.113..0.65 rows=20 loops=1)
    -> Filter: (movie.`status` = 1)  (cost=1.84 rows=2) (actual time=0.112..0.649 rows=20 loops=1)
        -> Index scan on movie using idx_rd  (cost=1.84 rows=20) (actual time=0.109..0.643 rows=66 loops=1)

```

release_date를 기준으로 스캔하다가 status = 1인 row를 20개 찾은 후에 Early Stopping

→ 지금은 status = 1인 행들이 많아서 운이 좋게 빠르게 끝났지만, genre와 같은 속성이였으면 더 길어졌음

→ 그래도 Sort가 없어서 위에보다는 빠를 듯

1. 복합 인덱스 (release_date, status)

```java
-> Limit: 20 row(s)  (cost=1.84 rows=2) (actual time=0.153..0.498 rows=20 loops=1)
    -> Filter: (movie.`status` = 1)  (cost=1.84 rows=2) (actual time=0.152..0.496 rows=20 loops=1)
        -> Index scan on movie using idx_rd_status  (cost=1.84 rows=20) (actual time=0.112..0.489 rows=66 loops=1)
```

1. 복합 인덱스 (status,release_date)

```java
-> Limit: 20 row(s)  (cost=786 rows=20) (actual time=0.437..0.446 rows=20 loops=1)
    -> Index lookup on movie using idx_status_rd (status=1) (reverse)  (cost=786 rows=6656) (actual time=0.436..0.444 rows=20 loops=1)
```

→ release_date를 먼저 적용하면 2번과 다를게 없음

→ status를 기준으로 release_date가 정렬되어 있어 정확히 20개만 읽음

---

```java
EXPLAIN ANALYZE
SELECT movie_id, title, status, genre
FROM movie
WHERE status = 1 AND genre = 3;
```

```java
-> Index lookup on movie using idx_status_genre (status=1, genre=3)  (cost=202 rows=811) (actual time=0.242..1.16 rows=811 loops=1)
```

1. 커버링 인덱스

```java
-> Covering index lookup on movie using covering_idx (status=1, genre=3)  (cost=182 rows=811) (actual time=0.0362..0.214 rows=811 loops=1)\n'
```
   
6. Deploy to Production (프로덕션 배포)
   
7. Monitor (모니터링)
