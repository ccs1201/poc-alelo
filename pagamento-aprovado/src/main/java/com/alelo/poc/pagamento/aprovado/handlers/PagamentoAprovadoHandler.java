package com.alelo.poc.pagamento.aprovado.handlers;


import com.alelo.poc.clients.constants.PagamentoConstants;
import com.alelo.poc.clients.events.PagamentoEvent;
import com.alelo.poc.pagamento.aprovado.services.CashBackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = PagamentoConstants.QUEUE_PAGAMENTOS_APROVADOS)
@RequiredArgsConstructor
@Slf4j
public class PagamentoAprovadoHandler {

    private final CashBackService cashBackService;

    @RabbitHandler
    public void handle(PagamentoEvent event) {
        try {
            log.info("Evento recebido: {}", event);
            cashBackService.processarCashBack(event);
        } catch (Exception e) {
            log.error("Erro ao processar evento: {}", event, e);
        }
    }
}
