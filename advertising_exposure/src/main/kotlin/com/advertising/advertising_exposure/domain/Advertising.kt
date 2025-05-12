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
    val advertisingBillingType: AdvertisingBillingType,
    var advertisingStatus: AdvertisingStatus?,
    var charge: BigDecimal?,
    val startAt: LocalDateTime,
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

    fun activate() {
        advertisingStatus = AdvertisingStatus.ACTIVE
    }

    fun deactivate() {
        advertisingStatus = AdvertisingStatus.FINISHED
    }

    companion object {
        operator fun invoke(
            advertisement: Advertisement,
            advertisingBillingType: AdvertisingBillingType,
            advertisingStatus: AdvertisingStatus?,
            charge: BigDecimal?,
            startAt: LocalDateTime
        ) =
            Advertising(
                id = null,
                advertisement = advertisement,
                advertisingBillingType = advertisingBillingType,
                advertisingStatus = advertisingStatus,
                charge = charge,
                startAt = startAt,
                endedAt = startAt.plusMonths(1),
                paymentDate = LocalDateTime.now()
            )
    }
}