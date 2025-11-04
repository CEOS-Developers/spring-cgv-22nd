# Build Stage
FROM gradle:8.5-jdk17 AS builder
ENV TZ=Asia/Seoul
WORKDIR /app

# gradle wrapper 관련 파일 명시적 복사
COPY gradlew ./gradlew
COPY gradle/wrapper/gradle-wrapper.jar ./gradle/wrapper/gradle-wrapper.jar
COPY gradle/wrapper/gradle-wrapper.properties ./gradle/wrapper/gradle-wrapper.properties

RUN chmod +x ./gradlew
RUN ls -al gradle/wrapper

COPY . .

# 빌드 실행
RUN ./gradlew clean build -x test --no-daemon

# Run Stage
FROM eclipse-temurin:17-jdk

ENV TZ=Asia/Seoul

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","app.jar"]