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

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoNegadoHandler {

    private final AtomicInteger msgProcessadosComSucesso = new AtomicInteger();
    private final RabbitTemplate rabbitTemplate;

    @PreDestroy
    public void printStats() {
        log.info("Total de mensagens processadas com sucesso: {}", msgProcessadosComSucesso.get());
    }

    @RabbitListener(queues = PagamentoConstants.QUEUE_PAGAMENTO_NEGADO, concurrency = "10")
    public void handle(@Payload PagamentoEventTest event) {
        log.info("Evento recebido: {}", event);
        msgProcessadosComSucesso.incrementAndGet();
        rabbitTemplate.convertAndSend(PagamentoConstants.QUEUE_NOTIFICACAO_PAGAMENTO,
                new NotificationEvent(event.idCartao(), event.valor(), TipoNotificaoEnum.PAGAMENTO_NEGADO));
    }
}
