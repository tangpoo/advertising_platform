package com.advertising.advertising_billing.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class Advertising(
    val id: Long?,
    var advertisement: Advertisement,
    val advertisingType: AdvertisingType,
    var charge: BigDecimal?,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val paymentDate: LocalDateTime
) {
    companion object {
        operator fun invoke(
            advertisement: Advertisement,
            advertisingType: AdvertisingType,
            charge: BigDecimal?,
            startedAt: LocalDateTime
        ) =
            Advertising(
                id = null,
                advertisement = advertisement,
                advertisingType = advertisingType,
                charge = charge,
                startedAt = startedAt,
                endedAt = startedAt.plusMonths(1),
                paymentDate = LocalDateTime.now()
            )
    }
}