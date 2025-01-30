package com.advertising.advertising_billing.subscriber

import com.advertising.advertising_billing.domain.Advertising
import com.advertising.advertising_billing.domain.AdvertisingType
import com.advertising.advertising_billing.domain.Billing
import com.advertising.advertising_billing.repository.BillingRepository
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class BillingMessageSubscriber(private val billingRepository: BillingRepository) {

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
    fun processBillingMessage(advertising: Advertising): Billing {
        println("Consuming advertising     ===>      $advertising")

        val price = calculatePrice(advertising)
        val shopId = advertising.advertisement.shopId
        val advertisingId = advertising.id ?: throw IllegalArgumentException("Advertising ID is null")

        return updateOrCreateBilling(shopId, price, advertisingId)
    }

    private fun calculatePrice(advertising: Advertising): BigDecimal {
        return when (advertising.advertisingType) {
            AdvertisingType.FLAT_RATE -> BigDecimal(10000)
            AdvertisingType.CHARGE -> advertising.charge ?: throw IllegalArgumentException("Charge cannot be null for charge type")
        }
    }

    private fun updateOrCreateBilling(shopId: Long, price: BigDecimal, advertisingId: Long): Billing {
        return billingRepository.findByShopId(shopId)
            .map { existingBilling ->
                val updatedBilling = existingBilling.withUpdatedIsPrice(existingBilling.price.add(price))
                billingRepository.save(updatedBilling)
            }
            .orElseGet {
                val newBilling = Billing(advertisingId = advertisingId, shopId = shopId, price = price)
                billingRepository.save(newBilling)
            }
    }
}