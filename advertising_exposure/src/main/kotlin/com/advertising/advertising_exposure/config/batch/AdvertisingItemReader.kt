package com.advertising.advertising_exposure.config.batch

import com.advertising.advertising_exposure.domain.Advertising
import com.advertising.advertising_exposure.domain.AdvertisingStatus
import com.advertising.advertising_exposure.repository.AdvertisingExposureRepository
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import java.time.LocalDateTime

@Configuration
class AdvertisingItemReader {

    @Bean
    @StepScope
    fun advertisingActivationItemReader(
        advertisingRepository: AdvertisingExposureRepository,
        @Value("#{T(java.time.LocalDateTime).now()}") now: LocalDateTime
    ): RepositoryItemReader<Advertising> {
        return RepositoryItemReaderBuilder<Advertising>()
            .name("advertisingActivationItemReader")
            .repository(advertisingRepository)
            .methodName("findByAdvertisingStatusAndStartAtLessThanEqual")
            .arguments(listOf(AdvertisingStatus.WAITING, now))
            .pageSize(100)
            .sorts(mapOf("id" to Sort.Direction.ASC))
            .build()
    }

    @Bean
    @StepScope
    fun advertisingDeactivationItemReader(
        advertisingRepository: AdvertisingExposureRepository,
        @Value("#{T(java.time.LocalDateTime).now()}") now: LocalDateTime
    ): RepositoryItemReader<Advertising> {
        return RepositoryItemReaderBuilder<Advertising>()
            .name("advertisingDeactivationItemReader")
            .repository(advertisingRepository)
            .methodName("findByAdvertisingStatusAndEndedAtLessThanEqual")
            .arguments(listOf(AdvertisingStatus.ACTIVE, now))
            .pageSize(100)
            .sorts(mapOf("id" to Sort.Direction.ASC))
            .build()
    }
}