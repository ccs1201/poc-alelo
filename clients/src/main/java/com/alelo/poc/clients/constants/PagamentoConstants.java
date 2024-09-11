package com.alelo.poc.clients.constants;

public class PagamentoConstants {

    private PagamentoConstants() {
    }


    private static final String DOMAIN = "pagamento.";

    public static final String QUEUE_PAGAMENTOS_APROVADOS = DOMAIN + "aprovado";
    public static final String EXCHANGE_PAGAMENTO = DOMAIN + "exchange";
    public static final String QUEUE_PAGAMENTO_NEGADO = DOMAIN + "negado";
    public static final String EXCHANGE_PAGAMENTO_DLX = DOMAIN + "exchange.dlx";
    public static final String QUEUE_PAGAMENTO_DLQ = DOMAIN + "dlq";
    public static final String QUEUE_NOTIFICACAO_PAGAMENTO = DOMAIN + "notificacao";
}
