package com.advertising.advertising_exposure.service

import com.advertising.advertising_exposure.controller.dto.AdvertisingReq
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingType
import com.advertising.advertising_exposure.repository.AdvertisementRepository
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import com.advertising.advertising_exposure.repository.search.AdvertisementQueryRepositoryImpl
import com.advertising.advertising_exposure.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argThat
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
    private lateinit var advertisementQueryRepositoryImpl: AdvertisementQueryRepositoryImpl

    @Test
    fun `post advertisement of charge type`() {
        // Arrange
        val advertisingReq =
            AdvertisingReq(1L, AdvertisingType.CHARGE, 100000L, LocalDateTime.now().plusDays(5))
        val advertisement = Advertisement(1L, null, "description", "seoul")

        val advertising = Advertising(
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
}