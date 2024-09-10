package com.alelo.poc.pagamentos.negados.handlers;


import com.alelo.poc.clients.constants.PagamentoConstants;
import com.alelo.poc.clients.events.NotificationEvent;
import com.alelo.poc.clients.events.enums.TipoNotificaoEnum;
import com.alelo.poc.pagamentos.negados.clients.PagamentoEventTest;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoNegadoHandler {

    private static final Random random = new SecureRandom();
    private final AtomicInteger msgProcessadosComSucesso = new AtomicInteger();
    private final AtomicInteger tentativas = new AtomicInteger();
    private final RabbitTemplate rabbitTemplate;

    @PreDestroy
    public void printStats() {
        log.info("Total de mensagens processadas com sucesso: {}", msgProcessadosComSucesso.get());
        log.info("Total de tentativas de processamento: {}", tentativas.get());
    }

    @RabbitListener(queues = PagamentoConstants.QUEUE_PAGAMENTO_NEGADO, concurrency = "10")
    public void handle(@Payload PagamentoEventTest event) {
        tentativas.incrementAndGet();

        if (random.nextBoolean()) {
            rabbitTemplate.convertAndSend(PagamentoConstants.QUEUE_NOTIFICACAO_PAGAMENTO,
                    new NotificationEvent(event.idCartao(), event.valor(), TipoNotificaoEnum.PAGAMENTO_NEGADO));
        }

        log.info("Evento recebido: {}", event);
        msgProcessadosComSucesso.incrementAndGet();
    }
}
