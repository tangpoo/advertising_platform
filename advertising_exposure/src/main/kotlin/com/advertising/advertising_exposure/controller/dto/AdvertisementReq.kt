package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.Advertisement

data class AdvertisementReq(
    val shopId: Long,
    val image: String?,
    val description: String,
    val region: String
) {
    fun toEntity() =
        Advertisement(
            shopId,
            image,
            description,
            region
        )
}
