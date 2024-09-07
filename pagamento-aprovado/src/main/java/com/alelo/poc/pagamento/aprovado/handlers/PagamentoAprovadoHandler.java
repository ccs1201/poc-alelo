package com.alelo.poc.pagamento.aprovado.handlers;


import com.alelo.poc.clients.constants.PagamentoConstants;
import com.alelo.poc.clients.events.PagamentoEvent;
import com.alelo.poc.pagamento.aprovado.services.CashBackService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RabbitListener(queues = PagamentoConstants.QUEUE_PAGAMENTOS_APROVADOS, concurrency = "10")
@RequiredArgsConstructor
@Slf4j
public class PagamentoAprovadoHandler {

    private final CashBackService cashBackService;
    private static final AtomicInteger msgProcessadosComSucesso = new AtomicInteger();

    @PreDestroy
    public void printStats() {
        log.info("Total de mensagens processadas com sucesso: {}", msgProcessadosComSucesso.get());
    }

    @RabbitHandler
    public void handle(PagamentoEvent event) {
        log.info("Evento recebido: {}", event);
        cashBackService.processarCashBack(event);
        msgProcessadosComSucesso.incrementAndGet();
    }
}
