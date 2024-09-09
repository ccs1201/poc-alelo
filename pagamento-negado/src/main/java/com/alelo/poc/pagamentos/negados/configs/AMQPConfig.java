package com.alelo.poc.pagamentos.negados.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AMQPConfig {

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        var msgConverter = new Jackson2JsonMessageConverter(new ObjectMapper()
                .registerModule(new JavaTimeModule()));

        msgConverter.setAlwaysConvertToInferredType(true);

        return msgConverter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter jackson2JsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(jackson2JsonMessageConverter);
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
