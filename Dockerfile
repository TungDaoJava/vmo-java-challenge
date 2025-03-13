FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean install -Dmaven.test.skip=true


FROM eclipse-temurin:21.0.5_11-jdk AS deploy
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]