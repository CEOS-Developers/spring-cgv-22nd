### 인덱스란?

데이터 검색 속도 향상을 위하여 테이블에 저장된 row를 식별가능하도록 저장한 데이터 오브젝트

인덱스는 언제 사용하는 게 좋을까?

→ 카디널리티가 높고 활용도가 높은 컬럼에서!

또는 규모가 크거나 삽입, 수정, 삭제가 자주 발생하지 않는 컬럼에서 사용하면 좋다.

### Clustered Index

: 데이터 레코드의 저장 순서를 인덱스 순서에 맞게 배치시킨다. 테이블당 하나만 적용 가능하며 주로 PK에 사용한다.

### Non-Clustered Index

: 인덱스와 실제 데이터의 저장 순서가 분리된다. 한 테이블에 여러 개 생성 가능하며 다양한 검색 조건 처리가 용이하다

### B-Tree Index

: 가장 일반적이고 보편적으로 사용하는 인덱스 구조로, 데이터가 정렬된 형태로 저장되며 범위 검색에 유용하다

### Hash Index

: 해시 함수를 활용해서 원하는 값을 빠르게 찾을 수 있으며 등가 비교에는 강하지만 범위 검색에는 적합하지 않다.

### Bitmap Index

: 컬럼의 종류가 적을 때 적합하다. 대량 읽기 작업에는 유리하나 빈번한 쓰기에는 부적합하다.

### Full-Text Index

### Composite Index

: 두 개 이상의 컬럼을 결합하여 인덱스를 생선한다. 하나의 조건 이상으로 자주 검색될 경우에 유용하다

### Covering Index

: 쿼리가 요청하는 모든 데이터가 인덱스에 포함되어 있어 테이블에 접근하지 않고도 인덱스만으로 결과 추출이 가능하다 → 디스크 I/O를 최소화하여 성능을 향상시킨다.

✅ 커버링 인덱스는 select, where, order by, group by 등에 쓰이는 컬럼이 인덱스에 모두 포함될 때 효과가 크다.

실행 계획 (EXPLAIN) - 쿼리의 맨 앞에 붙이면 실행 계획을 알 수 있다

### 성능 최적화 해보기

**Movie 테이블 인덱스 성능 비교 정리 (age_rating 기반 조회)**
성능을 비교할 쿼리

```sql
SELECT *
FROM movie
WHERE age_rating = 'ALL'
GROUP BY movie_id, genre;
```

사용한 인덱스

```sql
CREATE INDEX idx_movie_idx_id_genre
ON movie (age_rating, movie_id, genre);
```

인덱스 사용 전 후 비교

<img width="951" height="79" alt="스크린샷 2025-11-15 오전 2 42 53" src="https://github.com/user-attachments/assets/fec669c8-50e8-4622-9a90-479d907e786a" />

실행 전에는 Durationn이 0.00079sec, fetch가 0.000016 sec이었다.

실행 후에는 Duration이 0.0011sec, fetch가 0.000029 sec이었다

✅소량의 데이터라 인덱스 관리 비용때문에 오히려 느려보인다는 사실 확인

또한 MySql 옵티마이저가 인덱스보다 풀 스캔을 더 효율적이라고 판단한듯하다

그렇다면 인덱스는 데이터가 수천~수만 건일 때 효율적이라는 걸 확인

<img width="1115" height="602" alt="스크린샷 2025-11-15 오전 2 39 56" src="https://github.com/user-attachments/assets/ef92d258-759d-474c-8f39-85dc2b87c30e" />

실제로 데이터를 1200건 넣었고, 데이터가 많을 때는 인덱스가 효과적이라는 사실을 알 수 있었다

**커버링 인덱스 사용하기**

```sql
select movie_id, genre from movie where age_rating = 'ALL' GROUP BY movie_id, genre;
```

<img width="802" height="242" alt="image" src="https://github.com/user-attachments/assets/a800c507-2fb9-4da5-b36f-53f48a00bb5c" />

explain으로 확인한 결과 커버링 인덱스 사용하는 거 확인

<img width="642" height="58" alt="image" src="https://github.com/user-attachments/assets/19f88adc-6fae-4dd5-8807-b48991061eed" />

duration이 0.0010 → 0.00089, fetch가 0.000015 → 0.000014로 줄어든 걸 확인할 수 있었다.

**스케줄 조회 인덱스 성능 비교 By 복합키전략**

```sql
-- id가 100인 영화가 id가 20인 극장에서 향후 7일간 상영되는 스케줄 조회
SELECT
    s.schedule_id,
    s.start_time,
    s.end_time,
    s.movie_id,
    s.theater_id
FROM schedule s
WHERE s.movie_id = 100
  AND s.theater_id = 20
  AND s.start_time >= NOW()
  AND s.start_time < DATE_ADD(NOW(), INTERVAL 7 DAY)
ORDER BY s.start_time
LIMIT 50;
```

사용한 인덱스

```sql
CREATE INDEX idx_schedule_movie_theater_start 
ON schedule (movie_id, theater_id, start_time);
```

<img width="998" height="99" alt="image" src="https://github.com/user-attachments/assets/f35fb97b-3cbf-476d-bc3d-121e5ead547e" />


<img width="1023" height="72" alt="image" src="https://github.com/user-attachments/assets/b8d7ac6f-7bed-4215-9a55-860258446fee" />

duration은 0.0020 → 0.0012, fetch는 0.000016 → 0.000012로 줄어든 걸 확인할 수 있다!

