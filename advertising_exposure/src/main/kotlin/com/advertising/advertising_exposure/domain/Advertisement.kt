package com.advertising.advertising_exposure.domain

import com.advertising.advertising_exposure.event.AdvertisementEvent
import com.advertising.advertising_exposure.event.ApplicationContextProvider
import com.advertising.advertising_exposure.event.EventType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
class Advertisement private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val shopId: Long,
    val image: String?,
    val description: String,
    @CreationTimestamp
    val createdAt: LocalDateTime,
    val region: String,
    val isAllowed: Boolean,
    val minOrderPrice: Int?,
    val deliveryFee: Int?,
    val rating: Double?,
    val startedAt: LocalDateTime?
) {
    @PostPersist
    fun postPersist() {
        ApplicationContextProvider.publishEvent(
            AdvertisementEvent(this, EventType.CREATED)
        )
    }

    @PostUpdate
    fun postUpdate() {
        ApplicationContextProvider.publishEvent(
            AdvertisementEvent(this, EventType.UPDATED)
        )
    }

    @PostRemove
    fun postRemove() {
        ApplicationContextProvider.publishEvent(
            AdvertisementEvent(this, EventType.DELETED)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Advertisement
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    fun withUpdatedIsAllowed(isAllowed: Boolean): Advertisement {
        return Advertisement(
            id = this.id,
            shopId = this.shopId,
            image = this.image,
            description = this.description,
            createdAt = this.createdAt,
            region = this.region,
            isAllowed = isAllowed,
            minOrderPrice = this.minOrderPrice,
            deliveryFee = this.deliveryFee,
            rating = this.rating,
            startedAt = this.startedAt
        )
    }

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