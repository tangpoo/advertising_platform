package com.advertising.advertising_exposure.repository

import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.util.Streamable
import org.springframework.stereotype.Repository

@Repository
interface AdvertisingExposureRepository : JpaRepository<Advertising, Long> {
    fun findByAdvertisementIdInAndAdvertisingType(ids: List<Long>, advertisingType: AdvertisingType): List<Advertising>
}