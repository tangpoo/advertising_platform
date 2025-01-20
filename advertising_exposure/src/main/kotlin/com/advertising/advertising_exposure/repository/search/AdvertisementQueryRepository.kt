package com.advertising.advertising_exposure.repository.search

import com.advertising.advertising_exposure.domain.AdvertisementDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.util.Streamable

interface AdvertisementQueryRepository {
    fun filterAndSortAdvertisingInfos(
        minOrderPrice: Int?,
        maxDeliveryFee: Int?,
        sortBy: String,
        pageable: Pageable
    ): Streamable<AdvertisementDocument>
}