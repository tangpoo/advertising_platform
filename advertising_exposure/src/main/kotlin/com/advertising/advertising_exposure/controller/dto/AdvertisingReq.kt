package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingBillingType
import com.advertising.advertising_exposure.domain.AdvertisingStatus
import java.time.LocalDateTime

data class AdvertisingReq(
    val advertisementId: Long,
    val advertisingBillingType: AdvertisingBillingType,
    val charge: Long?,
    val startAt: LocalDateTime
) {
    fun toEntity(advertisement: Advertisement): Advertising {
        val status = if (startAt.isAfter(LocalDateTime.now())) {
            AdvertisingStatus.WAITING
        } else {
            AdvertisingStatus.ACTIVE
        }

        return Advertising(
            advertisement,
            advertisingBillingType,
            status,
            charge?.toBigDecimal(),
            startAt
        )
    }
}
