package com.advertising.advertising_exposure.controller.dto

import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.AdvertisementDocument
import org.springframework.data.util.Streamable
import java.time.LocalDateTime

data class AdvertisementRes(
    private val id: Long?,
    private val shopId: Long,
    private val image: String?,
    private val description: String,
    private val createAt: LocalDateTime,
    private val region: String,
    private val isAllowed: Boolean
) {
    companion object {
        fun fromEntity(advertisement: Advertisement) =
            AdvertisementRes(
                advertisement.id,
                advertisement.shopId,
                advertisement.image,
                advertisement.description,
                advertisement.createAt,
                advertisement.region,
                advertisement.isAllowed
            )

        fun fromDocument(advertisementDocument: AdvertisementDocument) =
            AdvertisementRes(
                advertisementDocument.id,
                advertisementDocument.shopId,
                advertisementDocument.image,
                advertisementDocument.description,
                advertisementDocument.createAt.toLocalDateTime(),
                advertisementDocument.region,
                advertisementDocument.isAllowed
            )

    }
}
