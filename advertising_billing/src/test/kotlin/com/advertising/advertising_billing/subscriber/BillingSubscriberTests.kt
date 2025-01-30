package com.advertising.advertising_billing.subscriber

import com.advertising.advertising_billing.domain.Advertisement
import com.advertising.advertising_billing.domain.Advertising
import com.advertising.advertising_billing.domain.AdvertisingType
import com.advertising.advertising_billing.domain.Billing
import com.advertising.advertising_billing.repository.BillingRepository
import com.advertising.advertising_exposure.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class BillingSubscriberTests {
    @InjectMocks
    private lateinit var billingMessageSubscriber: BillingMessageSubscriber

    @Mock
    private lateinit var billingRepository: BillingRepository

    @Test
    fun `should success when charge type advertising billing event when is exist`() {
        // Arrange
        val advertisement = createAdvertisement()
        val shopId = advertisement.shopId
        val advertising = createAdvertising(
            advertisement,
            AdvertisingType.CHARGE,
            BigDecimal(5000),
            LocalDateTime.now()
        )

        TestUtils.setId(advertising, "id", 1L)

        val billing = createBilling(shopId, advertising.id!!, BigDecimal(10000))

        `when`(billingRepository.findByShopId(shopId)).thenReturn(Optional.of(billing))
        `when`(billingRepository.save(any())).thenAnswer { it.arguments[0] as Billing }

        // Act
        val result = billingMessageSubscriber.processBillingMessage(advertising)

        // Assert
        assertEquals(BigDecimal(15000), result.price)
        assertEquals(billing.shopId, result.shopId)
        assertEquals(billing.advertisingId, result.advertisingId)
    }

    @Test
    fun `should success when charge type advertising billing event when is not exist`() {
        // Arrange
        val advertisement = createAdvertisement()
        val shopId = advertisement.shopId
        val advertising = createAdvertising(
            advertisement,
            AdvertisingType.CHARGE,
            BigDecimal(5000),
            LocalDateTime.now()
        )

        TestUtils.setId(advertising, "id", 1L)

        val billing = createBilling(shopId, advertising.id!!, BigDecimal(10000))

        `when`(billingRepository.findByShopId(shopId)).thenReturn(Optional.empty())
        `when`(billingRepository.save(any())).thenAnswer { it.arguments[0] as Billing }

        // Act
        val result = billingMessageSubscriber.processBillingMessage(advertising)

        // Assert
        assertEquals(BigDecimal(5000), result.price)
        assertEquals(billing.shopId, result.shopId)
        assertEquals(billing.advertisingId, result.advertisingId)
    }

    @Test
    fun `should success when flat rate type advertising billing event when is exist`() {
        // Arrange
        val advertisement = createAdvertisement()
        val shopId = advertisement.shopId
        val advertising = createAdvertising(
            advertisement,
            AdvertisingType.FLAT_RATE,
            BigDecimal(5000),
            LocalDateTime.now()
        )

        TestUtils.setId(advertising, "id", 1L)

        val billing = createBilling(shopId, advertising.id!!, BigDecimal(10000))

        `when`(billingRepository.findByShopId(shopId)).thenReturn(Optional.of(billing))
        `when`(billingRepository.save(any())).thenAnswer { it.arguments[0] as Billing }

        // Act
        val result = billingMessageSubscriber.processBillingMessage(advertising)

        // Assert
        assertEquals(BigDecimal(20000), result.price)
        assertEquals(billing.shopId, result.shopId)
        assertEquals(billing.advertisingId, result.advertisingId)
    }

    @Test
    fun `should success when flat rate type advertising billing event when is not exist`() {
        // Arrange
        val advertisement = createAdvertisement()
        val shopId = advertisement.shopId
        val advertising = createAdvertising(
            advertisement,
            AdvertisingType.FLAT_RATE,
            BigDecimal(5000),
            LocalDateTime.now()
        )

        TestUtils.setId(advertising, "id", 1L)

        val billing = createBilling(shopId, advertising.id!!, BigDecimal(10000))

        `when`(billingRepository.findByShopId(shopId)).thenReturn(Optional.empty())
        `when`(billingRepository.save(any())).thenAnswer { it.arguments[0] as Billing }

        // Act
        val result = billingMessageSubscriber.processBillingMessage(advertising)

        // Assert
        assertEquals(BigDecimal(10000), result.price)
        assertEquals(billing.shopId, result.shopId)
        assertEquals(billing.advertisingId, result.advertisingId)
    }

    @Test
    fun `should fail when charge is null for advertising type is charge type`() {
        // Arrange
        val advertisement = createAdvertisement()
        val advertising = createAdvertising(
            advertisement,
            AdvertisingType.CHARGE,
            null,
            LocalDateTime.now()
        )

        // Act
        val result = assertThrows<IllegalArgumentException> {
            billingMessageSubscriber.processBillingMessage(advertising)
        }

        // Assert
        assertEquals("Charge cannot be null for charge type", result.message)
    }


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

private fun createAdvertisement() = Advertisement(1L, null, "description", "seoul")

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

private fun createBilling(shopId: Long, advertisingId: Long, price: BigDecimal) =
    Billing(
        shopId,
        advertisingId,
        price
    )