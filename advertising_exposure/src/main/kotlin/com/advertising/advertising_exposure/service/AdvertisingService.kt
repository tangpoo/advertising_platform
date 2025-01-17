package com.advertising.advertising_exposure.service

import com.advertising.advertising_exposure.controller.dto.AdvertisingInfoReq
import com.advertising.advertising_exposure.controller.dto.AdvertisingInfoRes
import com.advertising.advertising_exposure.domain.AdvertisingInfo
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import com.advertising.advertising_exposure.repository.AdvertisingInfoRepository
import org.springframework.stereotype.Service

@Service
class AdvertisingService(
    private val advertisingInfoRepository: AdvertisingInfoRepository,
    private val advertisingExposureRepository: AdvertisingExposureRepository
) {
    fun saveAdvertisingInfo(advertisingInfoReq: AdvertisingInfoReq): AdvertisingInfoRes {
        val advertisingInfoEntity = advertisingInfoRepository.save(advertisingInfoReq.toEntity());
        return AdvertisingInfoRes.fromEntity(advertisingInfoEntity)
    }
}