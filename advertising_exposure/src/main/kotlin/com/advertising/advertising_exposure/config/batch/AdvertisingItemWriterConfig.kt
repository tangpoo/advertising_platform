package com.advertising.advertising_exposure.config.batch

import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.event.BillingEventPublisher
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdvertisingItemWriterConfig(
    private val advertisingExposureRepository: AdvertisingExposureRepository,
    private val advertisingEventPublisher: BillingEventPublisher
) {

    @Bean
    fun advertisingItemWriter(): ItemWriter<Advertising> =
        ItemWriter { items ->
            advertisingExposureRepository.saveAll(items)
            items.forEach { advertisingEventPublisher.sendBillingEvent(it) }
        }
}