package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingBillingType
import java.math.BigDecimal
import java.time.LocalDateTime

data class AdvertisingRes(
    val id: Long,
    val advertisingBillingType: AdvertisingBillingType,
    val charge: BigDecimal?,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val paymentDate: LocalDateTime
) {
    companion object {
        fun fromEntity(advertising: Advertising): AdvertisingRes =
            AdvertisingRes(
                advertising.id!!,
                advertising.advertisingBillingType,
                advertising.charge,
                advertising.startedAt,
                advertising.endedAt,
                advertising.paymentDate
            )
    }

}
