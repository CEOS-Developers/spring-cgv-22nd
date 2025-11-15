# 애플리케이션 빌드
FROM eclipse-temurin:17 AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

RUN chmod +x ./gradlew

RUN ./gradlew build

# 최종 실행 이미지 생성
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]