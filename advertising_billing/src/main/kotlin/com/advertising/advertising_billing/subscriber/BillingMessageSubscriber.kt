package com.advertising.advertising_billing.subscriber

import com.advertising.advertising_billing.domain.Advertising
import com.advertising.advertising_billing.domain.AdvertisingType
import com.advertising.advertising_billing.domain.Billing
import com.advertising.advertising_billing.repository.BillingRepository
import com.advertising.advertising_billing.service.BillingService
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class BillingMessageSubscriber(
    private val billingService: BillingService
) {

    companion object {
        private const val BILLING_EXCHANGE = "messageQueue.exchange.billing"

        private const val ROUTING_KEY_BILLING = "billing"
    }

    @RabbitListener(
        ackMode = "MANUAL", id = "addDeliveryMessageListener",
        bindings = [QueueBinding(
            value = Queue(),
            exchange = Exchange(BILLING_EXCHANGE),
            key = [ROUTING_KEY_BILLING]
        )]
    )
    fun processBillingMessage(advertising: Advertising) {
        println("Consuming advertising     ===>      $advertising")

        val price = billingService.calculatePrice(advertising)
        val shopId = advertising.advertisement.shopId
        val advertisingId = advertising.id ?: throw IllegalArgumentException("Advertising ID is null")

        billingService.updateOrCreateBilling(shopId, price, advertisingId)
    }
}