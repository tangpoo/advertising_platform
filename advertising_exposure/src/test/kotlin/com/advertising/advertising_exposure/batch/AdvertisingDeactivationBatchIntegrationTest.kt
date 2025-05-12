package com.advertising.advertising_exposure.batch

import com.advertising.advertising_exposure.batch.config.ActivationBatchTestConfig
import com.advertising.advertising_exposure.batch.config.DeactivationBatchTestConfig
import com.advertising.advertising_exposure.config.batch.AdvertisingDeactivationBatchJobConfig
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingBillingType
import com.advertising.advertising_exposure.domain.AdvertisingStatus
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import org.junit.jupiter.api.Assertions
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
@Import(DeactivationBatchTestConfig::class)
class AdvertisingDeactivationBatchIntegrationTest {

    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    lateinit var advertisingExposureRepository: AdvertisingExposureRepository

    @Test
    fun `만료 광고 비활성화 - ACTIVE 광고가 FINISH로 전환된다`() {
        // given
        val expiredAd = advertisingExposureRepository.save(
            Advertising(
                advertisement = createAdvertisement(),
                advertisingBillingType = AdvertisingBillingType.CHARGE,
                advertisingStatus = AdvertisingStatus.ACTIVE,
                charge = BigDecimal.TEN,
                startAt = LocalDateTime.now().minusMonths(1).minusMinutes(1),
            )
        )

        // when
        val jobExecution = jobLauncherTestUtils.launchJob()

        // then
        val updated = advertisingExposureRepository.findById(expiredAd.id!!).get()
        Assertions.assertEquals(AdvertisingStatus.FINISHED, updated.advertisingStatus)
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.status)
    }
}

private fun createAdvertisement() = Advertisement(
    1L,
    null,
    "description",
    "seoul"
)