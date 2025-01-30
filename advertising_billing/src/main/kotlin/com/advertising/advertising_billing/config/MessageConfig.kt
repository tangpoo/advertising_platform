package com.advertising.advertising_billing.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessageConfig {
    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter =
        Jackson2JsonMessageConverter(objectMapper.registerModule(JavaTimeModule()))
}