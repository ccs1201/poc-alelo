package com.alelo.poc.clients.events;

import com.alelo.poc.clients.events.enums.TipoNotificaoEnum;

import java.math.BigDecimal;

public record NotificationEvent(int clientId,
                                BigDecimal valor,
                                TipoNotificaoEnum tipoNotificao) {
}
