package com.alelo.teste.carga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@SpringBootApplication
@Slf4j
public class TesteCargaApplication {

    private static final int QTD_TESTS = 10_000;

    public static void main(String[] args) {
        SpringApplication.run(TesteCargaApplication.class, args);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create("http://localhost:8080/pagamentos");
    }

    @Bean
    public CommandLineRunner run(RestClient restClient) {

        var futures = new CompletableFuture[QTD_TESTS];

        log.info("Iniciando carga de teste");

        return args -> {
            for (int i = 0; i < QTD_TESTS; i++) {
                final int requestNumber = i;
                futures[i] = CompletableFuture.runAsync(() ->
                                restClient.post().retrieve().onStatus(status -> true,
                                        (request, response) ->
                                                log.info("Request {} status: {}", requestNumber, response.getStatusCode()))
                        , Executors.newVirtualThreadPerTaskExecutor());
            }

            CompletableFuture.allOf(futures).join();
            log.info("Carga finalizada");
            System.exit(0);
        };
    }

}
