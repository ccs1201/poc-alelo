package com.alelo.poc.pagamentos.negados.clients;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PagamentoEventTest(int idCartao,
                                 String nomeEstabelecimento,
                                 BigDecimal valor,
                                 OffsetDateTime dataHoraPagamento) {
}
