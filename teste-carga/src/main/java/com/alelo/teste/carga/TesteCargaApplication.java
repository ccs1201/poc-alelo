package com.alelo.teste.carga;

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
import java.util.logging.Logger;

@SpringBootApplication
public class TesteCargaApplication {

    private static final Logger log = Logger.getLogger(TesteCargaApplication.class.getName());
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

        var tempoFinal = System.currentTimeMillis() + (duracaoTeste * 1000);
        AtomicInteger requestNumber = new AtomicInteger(0);

        return args -> {
            while (System.currentTimeMillis() < tempoFinal){
                Thread.sleep(randon.nextInt(sleepTime));
                futures.add(CompletableFuture.runAsync(() -> {
                            try {
                                var response = restClient.post().retrieve();
                                counter.incrementAndGet();

                                log.info("Request %d status: %s".formatted(requestNumber.incrementAndGet(),
                                        response.toBodilessEntity().getStatusCode()));
                            } catch (Exception e) {
                                log.info(e.getMessage().concat(String.valueOf(requestNumber.incrementAndGet())));
                            }
                        }
                        , Executors.newVirtualThreadPerTaskExecutor()));

            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.info("Teste Carga finalizado Respostas recebidas %d".formatted(counter.get()));
            System.exit(0);
        };
    }

}
