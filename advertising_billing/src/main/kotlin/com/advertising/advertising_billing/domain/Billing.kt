package com.advertising.advertising_billing.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Billing private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val shopId: Long,
    val advertisingId: Long,
    val price: BigDecimal,
    val createdAt: LocalDateTime
) {
    fun withUpdatedIsPrice(price: BigDecimal) =
        Billing(
            this.id,
            this.shopId,
            this.advertisingId,
            price,
            this.createdAt
        )

    companion object {
        operator fun invoke(
            shopId: Long,
            advertisingId: Long,
            price: BigDecimal
        ) =
            Billing(
                null,
                shopId,
                advertisingId,
                price,
                LocalDateTime.now()
            )
    }
}