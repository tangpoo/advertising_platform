package com.advertising.advertising_exposure.batch

import com.advertising.advertising_exposure.batch.config.ActivationBatchTestConfig
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingBillingType
import com.advertising.advertising_exposure.domain.AdvertisingStatus
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(properties = [
    "spring.batch.job.enabled=false",
    "spring.batch.jdbc.initialize-schema=always"
])
@Import(ActivationBatchTestConfig::class)
class AdvertisingActivationBatchIntegrationTest {

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    lateinit var advertisingExposureRepository: AdvertisingExposureRepository

    @Test
    fun `예약 광고 활성화 Job - WAITING 광고가 ACTIVE로 전환된다`() {
        // given
        val ad = advertisingExposureRepository.save(
            Advertising(
                advertisement = createAdvertisement(),
                advertisingBillingType = AdvertisingBillingType.CHARGE,
                advertisingStatus = AdvertisingStatus.WAITING,
                charge = BigDecimal.TEN,
                startAt = LocalDateTime.now().minusSeconds(5)
            )
        )

        // when
        val jobExecution = jobLauncherTestUtils.launchJob()

        // then
        val updated = advertisingExposureRepository.findById(ad.id!!).get()
        assertEquals(AdvertisingStatus.ACTIVE, updated.advertisingStatus)
        assertEquals(BatchStatus.COMPLETED, jobExecution.status)
    }
}

private fun createAdvertisement() = Advertisement(
    1L,
    null,
    "description",
    "seoul"
)
