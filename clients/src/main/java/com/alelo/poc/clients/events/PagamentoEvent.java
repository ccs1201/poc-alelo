package com.alelo.poc.clients.events;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PagamentoEvent(int idCartao,
                             String nomeEstabelecimento,
                             BigDecimal valor,
                             OffsetDateTime dataHoraPagamento) {
}
