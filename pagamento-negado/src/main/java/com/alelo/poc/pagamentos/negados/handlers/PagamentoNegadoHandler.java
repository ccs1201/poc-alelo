package com.alelo.poc.pagamentos.negados.handlers;


import com.alelo.poc.clients.constants.PagamentoConstants;
import com.alelo.poc.clients.events.PagamentoEvent;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RabbitListener(queues = PagamentoConstants.QUEUE_PAGAMENTO_NEGADO, concurrency = "10")
@RequiredArgsConstructor
@Slf4j
public class PagamentoNegadoHandler {

    private static final Random random = new SecureRandom();
    private final AtomicInteger msgProcessadosComSucesso = new AtomicInteger();
    private final AtomicInteger tentativas = new AtomicInteger();

    @PreDestroy
    public void printStats() {
        log.info("Total de mensagens processadas com sucesso: {}", msgProcessadosComSucesso.get());
        log.info("Total de tentativas de processamento: {}", tentativas.get());
    }

    @RabbitHandler
    public void handle(PagamentoEvent event) {
        tentativas.incrementAndGet();
        if (random.nextBoolean()) {
            throw new RuntimeException("Erro ao processar o pagamento");
        }

        log.info("Evento recebido: {}", event);
        msgProcessadosComSucesso.incrementAndGet();
    }
}
