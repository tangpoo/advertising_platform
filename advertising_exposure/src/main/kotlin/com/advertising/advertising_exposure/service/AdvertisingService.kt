package com.advertising.advertising_exposure.service

import com.advertising.advertising_exposure.controller.dto.AdvertisementReq
import com.advertising.advertising_exposure.controller.dto.AdvertisementRes
import com.advertising.advertising_exposure.controller.dto.AdvertisingReq
import com.advertising.advertising_exposure.controller.dto.AdvertisingRes
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.AdvertisementDocument
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingType
import com.advertising.advertising_exposure.event.AdvertisementEvent
import com.advertising.advertising_exposure.event.BillingEventPublisher
import com.advertising.advertising_exposure.event.EventType
import com.advertising.advertising_exposure.repository.AdvertisementRepository
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import com.advertising.advertising_exposure.repository.search.AdvertisementQueryRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.util.Streamable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
@Transactional
class AdvertisingService(
    private val advertisementRepository: AdvertisementRepository,
    private val advertisingExposureRepository: AdvertisingExposureRepository,
    private val advertisingSearchRepository: AdvertisementQueryRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val billingEventPublisher: BillingEventPublisher
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
        val pageable = PageRequest.of(page, size)
        val advertisements = advertisingSearchRepository.filterAndSortAdvertisingInfos(
            minOrderPrice,
            maxDeliveryFee,
            sortBy,
            pageable
        )

        advertisements.map { it.id }
            .parseLong()
            .filterChargeTypeAdvertising()
            .deductChargeForChargeType()

        return advertisements.toResponse().toList()
    }
    fun postAdvertisement(advertisingReq: AdvertisingReq): AdvertisingRes {
        validateAdvertising(advertisingReq)

        // todo 광고비 산출 이벤트 발행
        val advertisement =
            advertisementRepository.findById(advertisingReq.advertisementId).orElseThrow()

        eventPublisher.publishEvent(
            AdvertisementEvent(
                advertisement,
                EventType.CREATED
            )
        )

        val advertising = advertisingReq.toEntity(advertisement)

        billingEventPublisher.sendBillingEvent(advertising)

        return AdvertisingRes.fromEntity(
            advertisingExposureRepository.save(
                advertising
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
    fun List<Advertising>.deductChargeForChargeType() {
        val toDeactivate = mutableListOf<Advertising>()

        this.forEach { advertising ->
            if (advertising.charge == null || advertising.charge!! < BigDecimal(1000)) {
                toDeactivate.add(advertising)
            } else {
                advertising.charge = advertising.charge!! - BigDecimal(500)
            }
        }

        advertisingExposureRepository.saveAll(this.filterNot { toDeactivate.contains(it) })

        toDeactivate.forEach { deactivateAdvertising(it) }
    }

    private fun deactivateAdvertising(advertising: Advertising) {
        val advertisement = advertising.advertisement
        val updatedAdvertisement = advertisement.withUpdatedIsAllowed(false)
        advertisementRepository.save(updatedAdvertisement)
        advertisingExposureRepository.delete(advertising)
        eventPublisher.publishEvent(
            AdvertisementEvent(
                advertisement,
                EventType.DELETED
            )
        )
    }
}

private fun Streamable<String>.parseLong(): Streamable<Long> =
    this.map { it.toLong() }

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

