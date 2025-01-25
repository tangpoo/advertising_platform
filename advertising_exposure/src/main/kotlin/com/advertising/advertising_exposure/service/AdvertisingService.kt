package com.advertising.advertising_exposure.service

import com.advertising.advertising_exposure.controller.dto.AdvertisementReq
import com.advertising.advertising_exposure.controller.dto.AdvertisementRes
import com.advertising.advertising_exposure.controller.dto.AdvertisingReq
import com.advertising.advertising_exposure.controller.dto.AdvertisingRes
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.AdvertisementDocument
import com.advertising.advertising_exposure.repository.AdvertisementRepository
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import com.advertising.advertising_exposure.repository.search.AdvertisementQueryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.util.Streamable
import org.springframework.stereotype.Service

@Service
class AdvertisingService(
    private val advertisementRepository: AdvertisementRepository,
    private val advertisingExposureRepository: AdvertisingExposureRepository,
    private val advertisingSearchRepository: AdvertisementQueryRepository,
) {
    fun saveAdvertisementInfo(advertisementReq: AdvertisementReq): AdvertisementRes {
        val advertisingInfoEntity = advertisementRepository.save(advertisementReq.toEntity())
        return AdvertisementRes.fromEntity(advertisingInfoEntity)
    }

    fun filterAndSortAdvertisementInfos(
        minOrderPrice: Int?,
        maxDeliveryFee: Int?,
        sortBy: String,
        page: Int,
        size: Int
    ): List<AdvertisementRes> {
        val pageable = PageRequest.of(page, size)
        return advertisingSearchRepository.filterAndSortAdvertisingInfos(
            minOrderPrice,
            maxDeliveryFee,
            sortBy,
            pageable
        )
            .toResponse()
            .toList()
    }

    fun postAdvertisement(advertisingReq: AdvertisingReq): AdvertisingRes {
        // todo AdvertisingType 확인 및 charge 검증
        // todo Advertisements 인덱싱 및 광고비 산출 이벤트 발행
        val advertisement = advertisementRepository.findById(advertisingReq.advertisementId).orElseThrow()
        return AdvertisingRes.fromEntity(
            advertisingExposureRepository.save(
                advertisingReq.toEntity(advertisement)
            )
        )
    }
}

fun Streamable<*>.toResponse(): Streamable<AdvertisementRes> {
    return this.map {
        when (it) {
            is Advertisement -> AdvertisementRes.fromEntity(it)
            is AdvertisementDocument -> AdvertisementRes.fromDocument(it)
            else -> throw IllegalArgumentException("Unsupported type: ${it::class}")
        }
    }
}

