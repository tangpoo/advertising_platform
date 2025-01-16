package com.advertising.advertising_exposure.repository

import com.advertising.advertising_exposure.domain.AdvertisingExposure
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdvertisingExposureRepository : JpaRepository<AdvertisingExposure, Long> {
}