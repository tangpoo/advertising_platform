package com.advertising.advertising_exposure.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class Advertising private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "advertising_info_id")
    var advertisement: Advertisement,
    val advertisingType: AdvertisingType,
    val charge: BigDecimal?,
    val startedAt: LocalDateTime,
    val endedAt: LocalDateTime,
    val paymentDate: LocalDateTime
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Advertising
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        operator fun invoke(
            advertisement: Advertisement,
            advertisingType: AdvertisingType,
            charge: BigDecimal,
            startedAt: LocalDateTime,
            paymentDate: LocalDateTime
        ) =
            Advertising(
                id = null,
                advertisement = advertisement,
                advertisingType = advertisingType,
                charge = charge,
                startedAt = startedAt,
                endedAt = startedAt.plusMonths(1),
                paymentDate = paymentDate
            )
    }
}