package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.AdvertisementDocument
import java.time.LocalDateTime

data class AdvertisementRes(
    val id: Long,
    private val shopId: Long,
    private val image: String?,
    private val description: String,
    private val createdAt: LocalDateTime?,
    private val region: String,
    private val isAllowed: Boolean
) {
    companion object {
        fun fromEntity(advertisement: Advertisement) =
            AdvertisementRes(
                advertisement.id!!,
                advertisement.shopId,
                advertisement.image,
                advertisement.description,
                advertisement.createdAt,
                advertisement.region,
                advertisement.isAllowed
            )

        fun fromDocument(advertisementDocument: AdvertisementDocument) =
            AdvertisementRes(
                advertisementDocument.id,
                advertisementDocument.shopId,
                advertisementDocument.image,
                advertisementDocument.description,
                advertisementDocument.createdAt,
                advertisementDocument.region,
                advertisementDocument.isAllowed
            )

    }
}
