FROM eclipse-temurin:21.0.4_7-jre-ubi9-minimal
LABEL authors="cleber.souza"
COPY target/pagamento-aprovado.jar app.jar
ENTRYPOINT ["java", "-XX:+UseShenandoahGC", "-Duser.language=pt", "-Duser.country=BR", "-jar", "/app.jar"]