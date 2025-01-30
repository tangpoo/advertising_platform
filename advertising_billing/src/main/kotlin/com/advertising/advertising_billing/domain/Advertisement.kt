package com.advertising.advertising_billing.domain

import java.time.LocalDateTime

data class Advertisement(
    val id: Long?,
    val shopId: Long,
    val image: String?,
    val description: String,
    val createdAt: LocalDateTime,
    val region: String,
    val isAllowed: Boolean,
    val minOrderPrice: Int?,
    val deliveryFee: Int?,
    val rating: Double?,
    val startedAt: LocalDateTime?
) {
    companion object {
        operator fun invoke(
            shopId: Long,
            image: String?,
            description: String,
            region: String,
        ) =
            Advertisement(
                id = null,
                shopId = shopId,
                image = image,
                description = description,
                createdAt = LocalDateTime.now(),
                region = region,
                isAllowed = false,
                minOrderPrice = null,
                deliveryFee = null,
                rating = null,
                startedAt = null
            )
    }
}