package com.advertising.advertising_billing.service

import com.advertising.advertising_billing.domain.Advertising
import com.advertising.advertising_billing.domain.AdvertisingType
import com.advertising.advertising_billing.domain.Billing
import com.advertising.advertising_billing.repository.BillingRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BillingService(private val billingRepository: BillingRepository) {
    fun calculatePrice(advertising: Advertising): BigDecimal {
        return when (advertising.advertisingType) {
            AdvertisingType.FLAT_RATE -> BigDecimal(10000)
            AdvertisingType.CHARGE -> advertising.charge ?: throw IllegalArgumentException("Charge cannot be null for charge type")
        }
    }

    fun updateOrCreateBilling(shopId: Long, price: BigDecimal, advertisingId: Long): Billing {
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