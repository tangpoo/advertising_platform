package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingType
import java.math.BigDecimal
import java.time.LocalDateTime

data class AdvertisingRes(
    val id: Long,
    val advertisingType: AdvertisingType,
    val charge: BigDecimal?,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val paymentDate: LocalDateTime
) {
    companion object {
        fun fromEntity(advertising: Advertising): AdvertisingRes =
            AdvertisingRes(
                advertising.id!!,
                advertising.advertisingType,
                advertising.charge,
                advertising.startedAt,
                advertising.endedAt,
                advertising.paymentDate
            )
    }

}
