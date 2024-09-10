package com.alelo.poc.server.configs;

import com.alelo.poc.clients.constants.PagamentoConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;

@Configuration
public class AMQPConfig {

    @Value("${server.message.ttl}")
    private static int ttl;

    private static Queue buildQueue(String queueName) {
        return QueueBuilder
                .durable(queueName)
                .deadLetterExchange(PagamentoConstants.EXCHANGE_PAGAMENTO_DLX)
//                .ttl(ttl)
                .maxPriority(10)
                .build();
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(Jackson2ObjectMapperBuilder
                .json()
                .dateFormat(DateFormat.getDateTimeInstance())
                .build()
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL));
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, TopicExchange exchange,
                                         MessageConverter jackson2JsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setExchange(exchange.getName());
        template.setMessageConverter(jackson2JsonMessageConverter);
        return template;
    }

    @Bean
    public TopicExchange exchange() {
        return ExchangeBuilder
                .topicExchange(PagamentoConstants.EXCHANGE_PAGAMENTO)
                .durable(true)
                .build();
    }

    @Bean
    public Queue queuePagamentoAprovado() {
        return buildQueue(PagamentoConstants.QUEUE_PAGAMENTOS_APROVADOS);
    }

    @Bean
    public Binding bindingPagamentoAprovado(Queue queuePagamentoAprovado, TopicExchange exchange) {
        return BindingBuilder
                .bind(queuePagamentoAprovado)
                .to(exchange)
                .with(PagamentoConstants.QUEUE_PAGAMENTOS_APROVADOS);
    }

    @Bean
    public Queue queuePagamentoNegado() {
        return buildQueue(PagamentoConstants.QUEUE_PAGAMENTO_NEGADO);
    }

    @Bean
    public Binding bindingPagamentoNegado(Queue queuePagamentoNegado, TopicExchange exchange) {
        return BindingBuilder
                .bind(queuePagamentoNegado)
                .to(exchange)
                .with(PagamentoConstants.QUEUE_PAGAMENTO_NEGADO);
    }

    @Bean
    public FanoutExchange exchangeDLX() {
        return ExchangeBuilder
                .fanoutExchange(PagamentoConstants.EXCHANGE_PAGAMENTO_DLX)
                .durable(true)
                .build();
    }

    @Bean
    public Queue pagamentoDLQ() {
        return QueueBuilder
                .durable(PagamentoConstants.QUEUE_PAGAMENTO_DLQ)
                .lazy()
                .build();
    }

    @Bean
    public Binding bindingPagamentoDLQ(Queue pagamentoDLQ, FanoutExchange exchangeDLQ) {
        return BindingBuilder
                .bind(pagamentoDLQ)
                .to(exchangeDLQ);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter jackson2JsonMessageConverter) {

        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(jackson2JsonMessageConverter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
