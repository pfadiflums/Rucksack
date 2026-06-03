FROM eclipse-temurin:21-alpine AS builder
WORKDIR /build
COPY . .
RUN chmod +x ./gradlew && ./gradlew :api:bootJar --no-daemon

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=builder /build/api/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]