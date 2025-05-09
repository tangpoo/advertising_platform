package com.advertising.advertising_exposure.repository

import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingBillingType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdvertisingExposureRepository : JpaRepository<Advertising, Long> {
    fun findByAdvertisementIdInAndAdvertisingType(ids: List<Long>, advertisingBillingType: AdvertisingBillingType): List<Advertising>
}