FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# Gradle 캐시 활용을 위한 의존성 먼저 다운로드
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY . .
RUN gradle clean build --no-daemon -x test

# 실행 스테이지
FROM openjdk:17-jdk-slim

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 환경 변수로 프로파일 설정 (기본값: docker)
ENV SPRING_PROFILES_ACTIVE=docker

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]



