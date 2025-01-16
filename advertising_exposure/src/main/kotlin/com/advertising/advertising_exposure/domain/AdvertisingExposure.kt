package com.advertising.advertising_exposure.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class AdvertisingExposure private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long?,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "advertising_info_id")
    private var advertisingInfo: AdvertisingInfo,
    private val advertisingType: AdvertisingType,
    private val charge: BigDecimal?,
    private val startedAt: LocalDateTime,
    private val endedAt: LocalDateTime,
    private val paymentDate: LocalDateTime
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as AdvertisingExposure
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        operator fun invoke(
            advertisingInfo: AdvertisingInfo,
            advertisingType: AdvertisingType,
            charge: BigDecimal,
            startedAt: LocalDateTime,
            paymentDate: LocalDateTime
        ) =
            AdvertisingExposure(
                id = null,
                advertisingInfo = advertisingInfo,
                advertisingType = advertisingType,
                charge = charge,
                startedAt = startedAt,
                endedAt = startedAt.plusMonths(1),
                paymentDate = paymentDate
            )
    }
}