package com.advertising.advertising_billing.subscriber

import com.advertising.advertising_billing.domain.AdvertisingType
import com.advertising.advertising_billing.repository.BillingRepository
import com.advertising.advertising_billing.service.BillingService
import com.advertising.advertising_exposure.util.createAdvertisement
import com.advertising.advertising_exposure.util.createAdvertising
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class BillingSubscriberTests {
    @InjectMocks
    private lateinit var billingMessageSubscriber: BillingMessageSubscriber

    @Mock
    private lateinit var billingRepository: BillingRepository

    @Mock
    private lateinit var billingService: BillingService

    @Test
    fun `should fail when advertising id is null`() {
        // Arrange
        val advertisement = createAdvertisement()
        val advertising = createAdvertising(
            advertisement,
            AdvertisingType.FLAT_RATE,
            BigDecimal(5000),
            LocalDateTime.now()
        )

        // Act
        val result = assertThrows<IllegalArgumentException> {
            billingMessageSubscriber.processBillingMessage(advertising)
        }

        // Assert
        assertEquals("Advertising ID is null", result.message)
    }
}

