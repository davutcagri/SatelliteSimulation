FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -B -q -DskipTests -pl ground-station-backend -am package

FROM eclipse-temurin:17-jre
COPY --from=build /app/ground-station-backend/target/ground-station-backend-0.1.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app.jar"]
