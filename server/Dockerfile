FROM eclipse-temurin:21.0.4_7-jre-ubi9-minimal
LABEL authors="cleber.souza"
COPY target/server.jar app.jar
ENTRYPOINT ["java", "-XX:+UseShenandoahGC", "-Xms1g", "-Xmx1850m", "-jar", "/app.jar"]
EXPOSE 8080