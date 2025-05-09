package com.advertising.advertising_exposure.service

import com.advertising.advertising_exposure.controller.dto.AdvertisementReq
import com.advertising.advertising_exposure.controller.dto.AdvertisementRes
import com.advertising.advertising_exposure.controller.dto.AdvertisingReq
import com.advertising.advertising_exposure.controller.dto.AdvertisingRes
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.AdvertisementDocument
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingBillingType.CHARGE
import com.advertising.advertising_exposure.domain.AdvertisingBillingType.FLAT_RATE
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
    fun saveAdvertisementInfo(advertisementReq: AdvertisementReq): AdvertisementRes =
        advertisementRepository.save(advertisementReq.toEntity()).let { AdvertisementRes.fromEntity(it) }

    fun filterAndSortAdvertisementInfos(
        minOrderPrice: Int?,
        maxDeliveryFee: Int?,
        sortBy: String,
        page: Int,
        size: Int
    ): List<AdvertisementRes> =
        // todo 클라이언트 관심사 외 이벤트로 분리
        advertisingSearchRepository.filterAndSortAdvertisingInfos(
            minOrderPrice,
            maxDeliveryFee,
            sortBy,
            PageRequest.of(page, size)
        ).let { advertisements ->
            advertisements.map { it.id }
                .parseLong()
                .filterChargeTypeAdvertising()
                .deductChargeForChargeType()

            advertisements.toResponse().toList()
        }

    fun postAdvertisement(advertisingReq: AdvertisingReq): AdvertisingRes =
        advertisingReq.apply { validateAdvertising(this) }
            .let { advertisementRepository.findById(it.advertisementId).orElseThrow() }
            .also { eventPublisher.publishEvent(AdvertisementEvent(it, EventType.CREATED)) }
            .let { advertisingReq.toEntity(it) }
            .let { advertisingExposureRepository.save(it) }
            .also {
                publishPaymentEventByType(it)
                billingEventPublisher.sendBillingEvent(it)
            }
            .let { AdvertisingRes.fromEntity(it) }

    private fun publishPaymentEventByType(advertisingEntity: Advertising) =
        when (advertisingEntity.advertisingBillingType) {
            CHARGE -> billingEventPublisher.sendImmediatePaymentEvent(advertisingEntity)
            FLAT_RATE -> billingEventPublisher.sendScheduledPaymentEvent(advertisingEntity)
        }

    private fun Streamable<Long>.filterChargeTypeAdvertising(): List<Advertising> =
        this.toList().takeIf { it.isNotEmpty() }?.let {
            advertisingExposureRepository.findByAdvertisementIdInAndAdvertisingType(it, CHARGE)
        } ?: emptyList()

    private fun List<Advertising>.deductChargeForChargeType() {
        val (toDeactivate, toUpdate) = this.partition {
            it.charge == null || it.charge!! < BigDecimal(
                1000
            )
        }
        toUpdate.forEach { it.charge = it.charge!! - BigDecimal(500) }
        advertisingExposureRepository.saveAll(toUpdate)
        toDeactivate.forEach(::deactivateAdvertising)
    }

    private fun deactivateAdvertising(advertising: Advertising) {
        advertisementRepository.save(advertising.advertisement.withUpdatedIsAllowed(false))
        advertisingExposureRepository.delete(advertising)
        eventPublisher.publishEvent(AdvertisementEvent(advertising.advertisement, EventType.DELETED))
    }
}

private fun Streamable<String>.parseLong(): Streamable<Long> =
    map(String::toLong)

private fun validateAdvertising(advertisingReq: AdvertisingReq) {
    when (advertisingReq.advertisingBillingType) {
        FLAT_RATE -> require(advertisingReq.charge == null) { "Charge must be null for flat rate type advertising." }
        CHARGE -> {
            require(advertisingReq.charge != null) { "Charge can not be null for charge type advertising." }
            require(advertisingReq.charge >= 500) { "Charge must be greater than 500 for charge type advertising." }
        }
    }
}

private fun Streamable<*>.toResponse(): Streamable<AdvertisementRes> = map {
    when (it) {
        is Advertisement -> AdvertisementRes.fromEntity(it)
        is AdvertisementDocument -> AdvertisementRes.fromDocument(it)
        else -> throw IllegalArgumentException("Unsupported type: ${it::class}")
    }
}

