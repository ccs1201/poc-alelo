package com.alelo.poc.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ServerApplicationTests {

    private static final int QTD_TESTS = 1_000;

    @LocalServerPort
    private Integer port;

    @Autowired
    private WebTestClient webTestClient;

//    @Test
    void contextLoads() {
        for (int i = 0; i < QTD_TESTS; i++) {
            CompletableFuture.runAsync(() ->
                            webTestClient.post()
                                    .uri("http://localhost:" + port + "/pagamentos")
                                    .exchange()
                                    .expectStatus().isCreated()
                    , Executors.newVirtualThreadPerTaskExecutor());
        }
    }

}
