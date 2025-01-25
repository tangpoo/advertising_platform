package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingType
import java.time.LocalDateTime

data class AdvertisingReq(
    val advertisementId: Long,
    val advertisingType: AdvertisingType,
    val charge: Long?,
    val startAt: LocalDateTime
) {
    fun toEntity(advertisement: Advertisement) =
        Advertising(
            advertisement,
            advertisingType,
            charge?.toBigDecimal(),
            startAt
        )
}
