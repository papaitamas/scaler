# Stage 1: Build the app using Gradle + JDK 17
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]