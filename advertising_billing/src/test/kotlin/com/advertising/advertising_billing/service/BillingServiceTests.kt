package com.advertising.advertising_billing.service

import com.advertising.advertising_billing.domain.AdvertisingType
import com.advertising.advertising_billing.repository.BillingRepository
import com.advertising.advertising_exposure.util.createAdvertisement
import com.advertising.advertising_exposure.util.createAdvertising
import com.advertising.advertising_exposure.util.createBilling
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class BillingServiceTests {
    @InjectMocks
    private lateinit var billingService: BillingService

    @Mock
    private lateinit var billingRepository: BillingRepository

    @Nested
    inner class CalculatePrice {
        @Test
        fun `should return 10000 for flat rate advertising`() {
            // given
            val advertising = createAdvertising(
                createAdvertisement(),
                AdvertisingType.FLAT_RATE,
                null,
                LocalDateTime.now().plusMonths(1)
            )

            // when
            val price = billingService.calculatePrice(advertising)

            // then
            assertEquals(BigDecimal(10000), price)
        }

        @Test
        fun `should return charge amount for charge type advertising`() {
            // given
            val advertising = createAdvertising(
                createAdvertisement(),
                AdvertisingType.CHARGE,
                BigDecimal(5000),
                LocalDateTime.now().plusMonths(1)
            )

            // when
            val price = billingService.calculatePrice(advertising)

            // then
            assertEquals(BigDecimal(5000), price)
        }

        @Test
        fun `should throw exception when charge is null for charge type advertising`() {
            // given
            val advertising = createAdvertising(
                createAdvertisement(),
                AdvertisingType.CHARGE,
                null,
                LocalDateTime.now().plusMonths(1)
            )

            // when & then
            val exception = assertThrows<IllegalArgumentException> {
                billingService.calculatePrice(advertising)
            }
            assertEquals("Charge cannot be null for charge type", exception.message)
        }
    }

    @Nested
    inner class UpdateOrCreateBilling {
        @Test
        fun `should update billing when shop ID exists`() {
            // given
            val shopId = 1L
            val price = BigDecimal(5000)
            val advertisingId = 100L
            val existingBilling = createBilling(shopId, 1L, BigDecimal(10000))

            `when`(billingRepository.findByShopId(shopId)).thenReturn(Optional.of(existingBilling))
            `when`(billingRepository.save(any())).thenAnswer { it.arguments[0] }

            // when
            val updatedBilling = billingService.updateOrCreateBilling(shopId, price, advertisingId)

            // then
            assertEquals(existingBilling.price.add(price), updatedBilling.price)
            verify(billingRepository, times(1)).save(any())
        }

        @Test
        fun `should create new billing when shop ID does not exist`() {
            // given
            val shopId = 2L
            val price = BigDecimal(7000)
            val advertisingId = 200L

            `when`(billingRepository.findByShopId(shopId)).thenReturn(Optional.empty())
            `when`(billingRepository.save(any())).thenAnswer { it.arguments[0] }

            // when
            val newBilling = billingService.updateOrCreateBilling(shopId, price, advertisingId)

            // then
            assertEquals(shopId, newBilling.shopId)
            assertEquals(price, newBilling.price)
            verify(billingRepository, times(1)).save(any())

        }
    }
}