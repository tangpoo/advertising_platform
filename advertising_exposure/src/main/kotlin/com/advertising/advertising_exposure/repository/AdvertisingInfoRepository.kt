package com.advertising.advertising_exposure.repository

import com.advertising.advertising_exposure.domain.AdvertisingInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdvertisingInfoRepository : JpaRepository<AdvertisingInfo, Long> {
}