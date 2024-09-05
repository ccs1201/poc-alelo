package com.alelo.poc.pagamentos.negados.handlers;


import com.alelo.poc.clients.constants.PagamentoConstants;
import com.alelo.poc.clients.events.PagamentoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = PagamentoConstants.QUEUE_PAGAMENTO_NEGADO, concurrency = "10")
@RequiredArgsConstructor
@Slf4j
public class PagamentoNegadoHandler {

    @RabbitHandler
    public void handle(PagamentoEvent event) {
            log.info("Evento recebido: {}", event);
    }
}
