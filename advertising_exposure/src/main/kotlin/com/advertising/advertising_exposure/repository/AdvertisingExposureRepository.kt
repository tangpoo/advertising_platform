package com.advertising.advertising_exposure.repository

import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingBillingType
import com.advertising.advertising_exposure.domain.AdvertisingStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AdvertisingExposureRepository : JpaRepository<Advertising, Long> {
    fun findByAdvertisementIdInAndAdvertisingBillingType(
        ids: List<Long>,
        advertisingBillingType: AdvertisingBillingType
    ): List<Advertising>

    fun findByAdvertisingStatusAndStartAtLessThanEqual(
        status: AdvertisingStatus,
        now: LocalDateTime,
        pageable: Pageable
    ): Page<Advertising>
}