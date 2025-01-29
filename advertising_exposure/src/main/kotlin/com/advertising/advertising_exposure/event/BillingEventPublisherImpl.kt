package com.advertising.advertising_exposure.event

import com.advertising.advertising_exposure.domain.Advertising
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Component

@Component
class BillingEventPublisherImpl(
    private val messageQueue: AmqpTemplate
) : BillingEventPublisher {

    companion object {
        private const val BILLING_EXCHANGE = "messageQueue.exchange.billing"

        private const val ROUTING_KEY_BILLING = "billing"
    }

    override fun sendBillingEvent(advertising: Advertising) {
        publishEvent(
            exchange = BILLING_EXCHANGE,
            routingKey = ROUTING_KEY_BILLING,
            advertising = advertising
        )
    }

    private fun publishEvent(
        exchange: String,
        routingKey: String,
        advertising: Advertising
    ): Advertising {
        messageQueue.convertAndSend(exchange, routingKey, advertising)
        return advertising
    }
}