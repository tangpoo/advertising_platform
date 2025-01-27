package com.advertising.advertising_exposure.service

import com.advertising.advertising_exposure.controller.dto.AdvertisementReq
import com.advertising.advertising_exposure.controller.dto.AdvertisementRes
import com.advertising.advertising_exposure.controller.dto.AdvertisingReq
import com.advertising.advertising_exposure.controller.dto.AdvertisingRes
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.AdvertisementDocument
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingType
import com.advertising.advertising_exposure.repository.AdvertisementRepository
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import com.advertising.advertising_exposure.repository.search.AdvertisementQueryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.util.Streamable
import org.springframework.stereotype.Service
import java.math.BigDecimal

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
        // todo 클라이언트 관심사 외 이벤트로 분리
        // todo 조회 후에 수수료 부족한 엔티티도 deactivate
        val pageable = PageRequest.of(page, size)
        val advertisements = advertisingSearchRepository.filterAndSortAdvertisingInfos(
            minOrderPrice,
            maxDeliveryFee,
            sortBy,
            pageable
        )

        advertisements.map { it.id }
            .filterChargeTypeAdvertising()
            .deductChargeForChargeType()

        return advertisements.toResponse().toList()
    }

    fun postAdvertisement(advertisingReq: AdvertisingReq): AdvertisingRes {
        validateAdvertising(advertisingReq)

        // todo Advertisements 인덱싱 및 광고비 산출 이벤트 발행
        val advertisement =
            advertisementRepository.findById(advertisingReq.advertisementId).orElseThrow()
        return AdvertisingRes.fromEntity(
            advertisingExposureRepository.save(
                advertisingReq.toEntity(advertisement)
            )
        )
    }

    private fun Streamable<Long>.filterChargeTypeAdvertising(): List<Advertising> {
        val ids = this.toList()

        if (ids.isEmpty()) return emptyList()
        return advertisingExposureRepository.findByAdvertisementIdInAndAdvertisingType(
            ids,
            AdvertisingType.CHARGE
        )
    }

    private fun List<Advertising>.deductChargeForChargeType() {
        this.forEach { advertising ->
            if (advertising.charge == null) {
                deactivateAdvertising(advertising)
            } else if (advertising.charge!! < BigDecimal(1000)) {
                deactivateAdvertising(advertising)
            } else {
                advertising.charge = advertising.charge!! - BigDecimal(500)
            }
        }
        advertisingExposureRepository.saveAll(this)
    }

    private fun deactivateAdvertising(advertising: Advertising) {
        val advertisement = advertising.advertisement
        val updatedAdvertisement = advertisement.withUpdatedIsAllowed(false)
        advertisementRepository.save(updatedAdvertisement)
        advertisingExposureRepository.delete(advertising)
    }
}


private fun validateAdvertising(advertisingReq: AdvertisingReq) {
    when (advertisingReq.advertisingType) {
        AdvertisingType.FLAT_RATE -> {
            if (advertisingReq.charge != null) {
                throw IllegalArgumentException("Charge must be null flat rate type advertising")
            }
        }

        AdvertisingType.CHARGE -> {
            val charge = advertisingReq.charge
                ?: throw IllegalArgumentException("Charge can not be null for charge type advertising")
            if (charge < 500) {
                throw IllegalArgumentException("Charge must be greater than 500 for charge type advertising")
            }
        }
    }
}

private fun Streamable<*>.toResponse(): Streamable<AdvertisementRes> = this.map {
    when (it) {
        is Advertisement -> AdvertisementRes.fromEntity(it)
        is AdvertisementDocument -> AdvertisementRes.fromDocument(it)
        else -> throw IllegalArgumentException("Unsupported type: ${it::class}")
    }
}

