FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
COPY src /app/src
COPY pom.xml /app
COPY mvnw /app
RUN mvn -f /app/pom.xml clean install -DskipTests

FROM openjdk:21-ea-23-jdk-bullseye
WORKDIR /app
COPY --from=build /app/target/*.jar /app/task-management.jar
ENTRYPOINT ["java", "-jar", "task-management.jar"]
