package com.advertising.advertising_exposure.util

import com.advertising.advertising_billing.domain.Advertisement
import com.advertising.advertising_billing.domain.Advertising
import com.advertising.advertising_billing.domain.AdvertisingType
import com.advertising.advertising_billing.domain.Billing
import org.springframework.test.util.ReflectionTestUtils
import java.math.BigDecimal
import java.time.LocalDateTime

class TestUtils {
    companion object {
        fun setId(target: Any, fieldName: String, value: Any) {
            ReflectionTestUtils.setField(target, fieldName, value)
        }
    }
}

fun createAdvertisement() = Advertisement(1L, null, "description", "seoul")

fun createAdvertising(
    advertisement: Advertisement,
    advertisingType: AdvertisingType,
    charge: BigDecimal?,
    startAt: LocalDateTime
) = Advertising(
    advertisement,
    advertisingType,
    charge,
    startAt
)

fun createBilling(shopId: Long, advertisingId: Long, price: BigDecimal) =
    Billing(
        shopId,
        advertisingId,
        price
    )