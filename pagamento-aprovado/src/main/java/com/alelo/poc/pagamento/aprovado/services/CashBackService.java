package com.alelo.poc.pagamento.aprovado.services;


import com.alelo.poc.clients.events.PagamentoEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CashBackService {
    public void processarCashBack(PagamentoEvent event) {
        log.info("Processando cashback para o pagamento: {}", event);
    }
}
