package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.AdvertisingInfo

data class AdvertisingInfoReq(
    val shopId: Long,
    val image: String?,
    val description: String,
    val region: String
) {
    fun toEntity() =
        AdvertisingInfo(
            shopId,
            image,
            description,
            region
        )
}
