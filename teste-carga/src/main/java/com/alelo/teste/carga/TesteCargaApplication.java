package com.alelo.teste.carga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Slf4j
public class TesteCargaApplication {

    private static int duracaoTeste = 10;
    private static int threadPoolSize = 50;
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        duracaoTeste = args.length > 0 ? Integer.parseInt(args[0]) : duracaoTeste;
        threadPoolSize = args.length > 1 ? Integer.parseInt(args[1]) : threadPoolSize;
        SpringApplication.run(TesteCargaApplication.class, args);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create("http://localhost:8080/pagamentos");
    }


    @Bean
    public static CommandLineRunner run(RestClient restClient) {
        var futures = new ArrayList<CompletableFuture<Void>>();

        log.info("Iniciando carga de teste");
        var tempoFinal = System.currentTimeMillis() + (duracaoTeste * 1000L);
        AtomicInteger requestNumber = new AtomicInteger(0);

        var executor = Executors.newFixedThreadPool(threadPoolSize);

        return args -> {
            while (System.currentTimeMillis() < tempoFinal) {
                futures.add(CompletableFuture.runAsync(() -> {
                    try {
                        var response = restClient.post().retrieve();

                        if (response.toBodilessEntity().getStatusCode().is2xxSuccessful()) {
                            counter.incrementAndGet();
                        }
                        log.info("Request {} status: {}", requestNumber.incrementAndGet(), response.toBodilessEntity().getStatusCode());

                    } catch (Exception e) {
                        log.info(e.getMessage().concat(" " + requestNumber.incrementAndGet()));
                    }
                }, executor));

            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            executor.shutdown();
            log.info("Teste carga finalizado.");
            log.info("Respostas de sucesso recebidas {}", (counter.get()));
            System.exit(0);
        };
    }
}

