package com.alelo.poc.server.services;


import com.alelo.poc.clients.constants.PagamentoConstants;
import com.alelo.poc.clients.events.PagamentoEvent;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoService {

    private final RabbitTemplate rabbitTemplate;
    private final Map<String, Integer> pagamentos = new ConcurrentHashMap<>();
    private final AtomicInteger contador = new AtomicInteger(0);

    @PreDestroy

    public void init() {
        log.info("Pagamentos aprovados: {}", pagamentos.get("aprovados"));
        log.info("Pagamentos negados: {}", pagamentos.get("negados"));
        log.info("Total: {}", pagamentos.get("negados") + pagamentos.get("aprovados"));
    }


    public void processarPagamento() {

        var pagamento = criarPagamento();

        if (aprovarPagamento()) {
            pagamentos.compute("aprovados", (k, v) -> v == null ? 1 : v + 1);
            rabbitTemplate.convertAndSend(PagamentoConstants.QUEUE_PAGAMENTOS_APROVADOS, pagamento);
            log.info("Pagamento aprovado: {}", pagamento);
            return;
        }

        pagamentos.compute("negados", (k, v) -> v == null ? 1 : v + 1);
        rabbitTemplate.convertAndSend(PagamentoConstants.QUEUE_PAGAMENTO_NEGADO, pagamento);
        log.info("Pagamento negado: {}", pagamento);
    }

    private static boolean aprovarPagamento() {
        return Math.random() > 0.5;
    }

    private PagamentoEvent criarPagamento() {
        return new PagamentoEvent(contador.incrementAndGet(),
                "Teste poc alelo",
                gerarValorAleotorio(),
                OffsetDateTime.now());
    }

    private static BigDecimal gerarValorAleotorio() {
        return BigDecimal.valueOf(Math.random() * 100).setScale(2, RoundingMode.HALF_UP);
    }
}
