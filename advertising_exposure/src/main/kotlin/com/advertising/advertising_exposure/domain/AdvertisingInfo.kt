package com.advertising.advertising_exposure.domain

import com.advertising.advertising_exposure.controller.dto.AdvertisingInfoReq
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
class AdvertisingInfo private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long?,
    private val shopId: Long,
    private val image: String?,
    private val description: String,
    @CreationTimestamp
    private val createAt: LocalDateTime,
    private val region: String,
    private val isAllowed: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as AdvertisingInfo
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        operator fun invoke(
            advertisingInfoReq: AdvertisingInfoReq
        ) =
            AdvertisingInfo(
                id = null,
                shopId = advertisingInfoReq.shopId,
                image = advertisingInfoReq.image,
                description = advertisingInfoReq.description,
                createAt = LocalDateTime.now(),
                region = advertisingInfoReq.region,
                isAllowed = false
            )
    }
}