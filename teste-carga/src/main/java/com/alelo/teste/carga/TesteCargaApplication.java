package com.alelo.teste.carga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Slf4j
public class TesteCargaApplication {

    private static int duracaoTeste = 10;
    private static int sleepTime = 2;
    private final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        duracaoTeste = args.length > 0 ? Integer.parseInt(args[0]) : duracaoTeste;
        sleepTime = args.length > 1 ? Integer.parseInt(args[1]) : sleepTime;
        SpringApplication.run(TesteCargaApplication.class, args);
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create("http://localhost:8080/pagamentos");
    }


    @Bean
    public CommandLineRunner run(RestClient restClient) {
        var futures = new ArrayList<CompletableFuture<Void>>();

        var randon = new SecureRandom();

        log.info("Iniciando carga de teste");

        var tempoFinal = System.currentTimeMillis() + (duracaoTeste * 1000L);
        AtomicInteger requestNumber = new AtomicInteger(0);

        return args -> {
            while (System.currentTimeMillis() < tempoFinal) {
                var sleep = randon.nextInt(sleepTime);

                if (sleep > 0) {
                    Thread.sleep(sleep);
                } else {
                    Thread.sleep(0, randon.nextInt(250_000, 999_999));
                }

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
                        }
                        , Executors.newVirtualThreadPerTaskExecutor()));

            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.info("Teste carga finalizado.");
            log.info("Respostas de sucesso recebidas {}", (counter.get()));
            System.exit(0);
        };
    }

}
