package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.AdvertisingInfo
import java.time.LocalDateTime

data class AdvertisingInfoRes(
    private val id: Long?,
    private val shopId: Long,
    private val image: String?,
    private val description: String,
    private val createAt: LocalDateTime,
    private val region: String,
    private val isAllowed: Boolean
) {
    companion object {
        fun fromEntity(advertisingInfo: AdvertisingInfo) =
            AdvertisingInfoRes(
                advertisingInfo.id,
                advertisingInfo.shopId,
                advertisingInfo.image,
                advertisingInfo.description,
                advertisingInfo.createAt,
                advertisingInfo.region,
                advertisingInfo.isAllowed
            )
    }
}
