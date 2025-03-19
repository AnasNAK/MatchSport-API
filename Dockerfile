FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .

ENTRYPOINT ["java", "-jar", "/app/MatchSport-API-0.0.1-SNAPSHOT.jar"]