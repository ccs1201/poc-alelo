package com.alelo.poc.server.configs;

import com.alelo.poc.clients.constants.PagamentoConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AMQPConfig {

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper()
                .registerModule(new JavaTimeModule()));
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
        return ExchangeBuilder.topicExchange(PagamentoConstants.EXCHANGE_PAGAMENTOS)
                .durable(true)
                .build();
    }

    @Bean
    public Queue queuePagamentoAprovado() {
        return QueueBuilder
                .durable(PagamentoConstants.QUEUE_PAGAMENTOS_APROVADOS)
                .build();
    }

    @Bean
    public Queue queuePagamentoNegado() {
        return QueueBuilder
                .durable(PagamentoConstants.QUEUE_PAGAMENTO_NEGADO)
                .build();
    }

    @Bean
    public Binding bindingPagamentoAprovado(Queue queuePagamentoAprovado, TopicExchange exchange) {
        return BindingBuilder
                .bind(queuePagamentoAprovado)
                .to(exchange)
                .with(PagamentoConstants.QUEUE_PAGAMENTOS_APROVADOS);
    }

    @Bean
    public Binding bindingPagamentoNegado(Queue queuePagamentoNegado, TopicExchange exchange) {
        return BindingBuilder
                .bind(queuePagamentoNegado)
                .to(exchange)
                .with(PagamentoConstants.QUEUE_PAGAMENTO_NEGADO);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                                     Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(jackson2JsonMessageConverter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
