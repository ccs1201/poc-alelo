#FROM amazoncorretto:21.0.4-alpine3.20
FROM eclipse-temurin:21.0.4_7-jre-ubi9-minimal
LABEL authors="cleber.souza"
COPY target/server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:+UseShenandoahGC", "-Xms1g", "-Xmx1850m", "-jar", "/app.jar"]
EXPOSE 8080