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
    val id: Long?,
    val shopId: Long,
    val image: String?,
    val description: String,
    @CreationTimestamp
    val createAt: LocalDateTime,
    val region: String,
    val isAllowed: Boolean
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
            shopId: Long,
            image: String?,
            description: String,
            region: String,
        ) =
            AdvertisingInfo(
                id = null,
                shopId = shopId,
                image = image,
                description = description,
                createAt = LocalDateTime.now(),
                region = region,
                isAllowed = false
            )
    }
}