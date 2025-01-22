package com.advertising.advertising_exposure.repository

import com.advertising.advertising_exposure.domain.Advertisement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdvertisementRepository : JpaRepository<Advertisement, Long> {
}