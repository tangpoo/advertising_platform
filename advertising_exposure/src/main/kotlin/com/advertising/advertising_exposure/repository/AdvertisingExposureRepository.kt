package com.advertising.advertising_exposure.repository

import com.advertising.advertising_exposure.domain.Advertising
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdvertisingExposureRepository : JpaRepository<Advertising, Long> {
}