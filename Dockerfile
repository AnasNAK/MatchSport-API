FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "MatchSport-API-0.0.1-SNAPSHOT.jar"]
