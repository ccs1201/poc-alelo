package com.alelo.poc.notification.handlers;

import com.alelo.poc.clients.constants.PagamentoConstants;
import com.alelo.poc.clients.events.NotificationEvent;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationHandler {

    private final AtomicInteger counter = new AtomicInteger(0);

    @PreDestroy
    public void printStats() {
        log.info("Total de notificações recebidas: {}", counter.get());
    }

    @RabbitListener(queues = PagamentoConstants.QUEUE_NOTIFICACAO_PAGAMENTO, concurrency = "10")
    public void processarNotificacao(NotificationEvent message) {
        log.info("Notificação recebida: {}", message);
        counter.incrementAndGet();
    }
}
