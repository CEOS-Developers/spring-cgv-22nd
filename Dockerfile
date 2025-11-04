FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
