package com.advertising.advertising_exposure.service

import com.advertising.advertising_exposure.controller.dto.AdvertisementRes
import com.advertising.advertising_exposure.controller.dto.AdvertisingReq
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.AdvertisementDocument
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingType
import com.advertising.advertising_exposure.repository.AdvertisementRepository
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import com.advertising.advertising_exposure.repository.search.AdvertisementQueryRepository
import com.advertising.advertising_exposure.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.springframework.data.domain.PageRequest
import org.springframework.data.util.Streamable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class AdvertisingServiceTests {
    @InjectMocks
    private lateinit var advertisingService: AdvertisingService

    @Mock
    private lateinit var advertisementRepository: AdvertisementRepository

    @Mock
    private lateinit var advertisingExposureRepository: AdvertisingExposureRepository

    @Mock
    private lateinit var advertisementSearchRepository: AdvertisementQueryRepository


    @Nested
    inner class PostAdvertisement {
        @Test
        fun `Success post advertisement of charge type`() {
            // Arrange
            val advertisingReq =
                AdvertisingReq(1L, AdvertisingType.CHARGE, 100000L, LocalDateTime.now().plusDays(5))
            val advertisement = createAdvertisement()

            val advertising = createAdvertising(
                advertisement,
                advertisingReq.advertisingType,
                advertisingReq.charge?.toBigDecimal(),
                advertisingReq.startAt
            )
            TestUtils.setId(advertising, "id", 1L)

            `when`(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement))
            `when`(advertisingExposureRepository.save(argThat {
                this.advertisement == advertising.advertisement
            })).thenReturn(
                advertising
            )

            // Act
            val result = advertisingService.postAdvertisement(advertisingReq)

            // Assert
            assertEquals(advertisingReq.advertisingType, result.advertisingType)
            assertEquals(advertisingReq.charge?.toBigDecimal(), result.charge)
        }

        @Test
        fun `should fail when charge is null for charge type advertising`() {
            // Arrange
            val advertisingReq =
                AdvertisingReq(1L, AdvertisingType.CHARGE, null, LocalDateTime.now().plusDays(5))

            // Act + Assert
            val result = assertThrows<IllegalArgumentException> {
                advertisingService.postAdvertisement(advertisingReq)
            }
            assertEquals("Charge can not be null for charge type advertising", result.message)
        }
    }

    @Nested
    inner class SearchAdvertisementInfos {
        @Test
        fun `should fail when charge is null for charge type advertising`() {
            // Arrange
            val minOrderPrice = null
            val maxDeliveryFee = null
            val sortBy = "createdAt"
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)

            val advertisementDocumentList = createAdvertisementDocumentStreamable()
            val advertising = createAdvertising(
                createAdvertisement(),
                AdvertisingType.CHARGE,
                null,
                LocalDateTime.now()
            )
            val advertisingList = listOf(advertising)
            val advertisement = advertising.advertisement
            val updateAdvertisement = advertisement.withUpdatedIsAllowed(false)

            `when`(
                advertisementSearchRepository.filterAndSortAdvertisingInfos(
                    minOrderPrice,
                    maxDeliveryFee,
                    sortBy,
                    pageable
                )
            ).thenReturn(advertisementDocumentList)

            `when`(
                advertisingExposureRepository.findByAdvertisementIdInAndAdvertisingType(
                    any(),
                    eq(AdvertisingType.CHARGE)
                )
            ).thenReturn(advertisingList)

            `when`(
                advertisementRepository.save(updateAdvertisement)
            ).thenReturn(updateAdvertisement)

            `when`(
                advertisingExposureRepository.saveAll(advertisingList)
            ).thenReturn(advertisingList)

            // Act
            advertisingService.filterAndSortAdvertisementInfos(
                minOrderPrice,
                maxDeliveryFee,
                sortBy,
                page,
                size
            )

            // Assert
            assertEquals(false, updateAdvertisement.isAllowed)
            verify(advertisementRepository, times(1)).save(updateAdvertisement)
            verify(advertisingExposureRepository, times(1)).delete(advertising)

        }

        @Test
        fun `disable advertisement when insufficient charge for after search`() {
            // Arrange
            val minOrderPrice = null
            val maxDeliveryFee = null
            val sortBy = "createdAt"
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)

            val advertisementDocumentList = createAdvertisementDocumentStreamable()
            val advertising = createAdvertising(
                createAdvertisement(),
                AdvertisingType.CHARGE,
                BigDecimal(999),
                LocalDateTime.now()
            )
            val advertisingList = listOf(advertising)
            val advertisement = advertising.advertisement
            val updateAdvertisement = advertisement.withUpdatedIsAllowed(false)

            `when`(
                advertisementSearchRepository.filterAndSortAdvertisingInfos(
                    minOrderPrice,
                    maxDeliveryFee,
                    sortBy,
                    pageable
                )
            ).thenReturn(advertisementDocumentList)

            `when`(
                advertisingExposureRepository.findByAdvertisementIdInAndAdvertisingType(
                    any(),
                    eq(AdvertisingType.CHARGE)
                )
            ).thenReturn(advertisingList)

            `when`(
                advertisementRepository.save(updateAdvertisement)
            ).thenReturn(updateAdvertisement)

            `when`(
                advertisingExposureRepository.saveAll(advertisingList)
            ).thenReturn(advertisingList)

            // Act
            advertisingService.filterAndSortAdvertisementInfos(
                minOrderPrice,
                maxDeliveryFee,
                sortBy,
                page,
                size
            )

            // Assert
            assertEquals(BigDecimal(999), advertising.charge)
            assertEquals(false, updateAdvertisement.isAllowed)
            verify(advertisementRepository, times(1)).save(updateAdvertisement)
            verify(advertisingExposureRepository, times(1)).delete(advertising)
        }

        @Test
        fun `success search advertising for charge type`() {
            // Arrange
            val minOrderPrice = null
            val maxDeliveryFee = null
            val sortBy = "createdAt"
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)

            val advertisementDocumentList = createAdvertisementDocumentStreamable()
            val advertisingList = createAdvertisingList()

            `when`(
                advertisementSearchRepository.filterAndSortAdvertisingInfos(
                    minOrderPrice,
                    maxDeliveryFee,
                    sortBy,
                    pageable
                )
            ).thenReturn(advertisementDocumentList)
            `when`(
                advertisingExposureRepository.findByAdvertisementIdInAndAdvertisingType(
                    any(),
                    eq(AdvertisingType.CHARGE)
                )
            ).thenReturn(advertisingList)
            `when`(
                advertisingExposureRepository.saveAll(advertisingList)
            ).thenReturn(advertisingList)

            // Act
            val result = advertisingService.filterAndSortAdvertisementInfos(
                minOrderPrice,
                maxDeliveryFee,
                sortBy,
                page,
                size
            )

            // Assert
            assertEquals(result.size, 3)
            assertEquals(result.get(0).region, "seoul")
            assertEquals(result.get(1).region, "busan")
        }

        @Test
        fun `success search advertising for flat type`() {
            // Arrange
            val minOrderPrice = null
            val maxDeliveryFee = null
            val sortBy = "createdAt"
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)

            val advertisementDocumentList = createAdvertisementDocumentStreamable()

            `when`(
                advertisementSearchRepository.filterAndSortAdvertisingInfos(
                    minOrderPrice,
                    maxDeliveryFee,
                    sortBy,
                    pageable
                )
            ).thenReturn(advertisementDocumentList)
            `when`(
                advertisingExposureRepository.findByAdvertisementIdInAndAdvertisingType(
                    any(),
                    eq(AdvertisingType.CHARGE)
                )
            ).thenReturn(emptyList())
            `when`(
                advertisingExposureRepository.saveAll(emptyList())
            ).thenReturn(emptyList())

            // Act
            val result = advertisingService.filterAndSortAdvertisementInfos(
                minOrderPrice,
                maxDeliveryFee,
                sortBy,
                page,
                size
            )

            // Assert
            assertEquals(result.size, 3)
            assertEquals(result.get(0).region, "seoul")
            assertEquals(result.get(1).region, "busan")
        }

        @Test
        fun `return null when search is nothing`() {
            // Arrange
            val minOrderPrice = null
            val maxDeliveryFee = null
            val sortBy = "createdAt"
            val page = 0
            val size = 10
            val pageable = PageRequest.of(page, size)

            `when`(
                advertisementSearchRepository.filterAndSortAdvertisingInfos(
                    minOrderPrice,
                    maxDeliveryFee,
                    sortBy,
                    pageable
                )
            ).thenReturn(Streamable.empty())

            `when`(
                advertisingExposureRepository.saveAll(emptyList())
            ).thenReturn(emptyList())

            // Act
            val result = advertisingService.filterAndSortAdvertisementInfos(
                minOrderPrice,
                maxDeliveryFee,
                sortBy,
                page,
                size
            )

            // Assert
            assertEquals(emptyList<AdvertisementRes>(), result)
        }
    }
}

private fun createAdvertisement() = Advertisement(1L, null, "description", "seoul")

private fun createAdvertisementDocument() = AdvertisementDocument(
    1L,
    1L,
    "image",
    "description",
    LocalDateTime.now(),
    "seoul",
    true,
    null,
    null,
    null,
    LocalDateTime.now().plusDays(5)
)

private fun createAdvertisementDocumentList() = listOf(
    AdvertisementDocument(
        1L,
        1L,
        "image1",
        "description1",
        LocalDateTime.now(),
        "seoul",
        true,
        null,
        null,
        null,
        LocalDateTime.now().plusDays(5)
    ),
    AdvertisementDocument(
        2L,
        2L,
        "image2",
        "description2",
        LocalDateTime.now(),
        "busan",
        true,
        null,
        null,
        null,
        LocalDateTime.now().plusDays(5)
    ),
    AdvertisementDocument(
        3L,
        3L,
        "image3",
        "description3",
        LocalDateTime.now(),
        "daejeon",
        true,
        null,
        null,
        null,
        LocalDateTime.now().plusDays(5)
    )
)

private fun createAdvertisementDocumentStreamable() = Streamable.of(
    AdvertisementDocument(
        1L,
        1L,
        "image1",
        "description1",
        LocalDateTime.now(),
        "seoul",
        true,
        null,
        null,
        null,
        LocalDateTime.now().plusDays(5)
    ),
    AdvertisementDocument(
        2L,
        2L,
        "image2",
        "description2",
        LocalDateTime.now(),
        "busan",
        true,
        null,
        null,
        null,
        LocalDateTime.now().plusDays(5)
    ),
    AdvertisementDocument(
        3L,
        3L,
        "image3",
        "description3",
        LocalDateTime.now(),
        "daejeon",
        true,
        null,
        null,
        null,
        LocalDateTime.now().plusDays(5)
    )
)

private fun createAdvertising(
    advertisement: Advertisement,
    advertisingType: AdvertisingType,
    charge: BigDecimal?,
    startAt: LocalDateTime
) = Advertising(
    advertisement,
    advertisingType,
    charge,
    startAt
)

private fun createAdvertisingList() = listOf(
    createAdvertising(
        createAdvertisement(),
        AdvertisingType.CHARGE,
        BigDecimal(10000),
        LocalDateTime.now()
    ),
    createAdvertising(
        createAdvertisement(),
        AdvertisingType.CHARGE,
        BigDecimal(50000),
        LocalDateTime.now()
    ),
    createAdvertising(
        createAdvertisement(),
        AdvertisingType.CHARGE,
        BigDecimal(100000),
        LocalDateTime.now()
    ),
    createAdvertising(createAdvertisement(), AdvertisingType.FLAT_RATE, null, LocalDateTime.now()),
    createAdvertising(
        createAdvertisement(),
        AdvertisingType.FLAT_RATE,
        BigDecimal(100000),
        LocalDateTime.now()
    )
)