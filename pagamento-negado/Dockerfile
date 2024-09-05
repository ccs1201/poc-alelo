FROM eclipse-temurin:21.0.4_7-jre-ubi9-minimal
LABEL authors="cleber.souza"
COPY target/pagamento-negado-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:+UseShenandoahGC", "-jar", "/app.jar"]