package com.advertising.advertising_exposure.config.batch

import com.advertising.advertising_exposure.domain.Advertising
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class AdvertisingDeactivationBatchJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    @Qualifier("advertisingDeactivationItemReader") private val advertisingItemReader: ItemReader<Advertising>,
    @Qualifier("advertisingDeactivationItemProcessor") private val advertisingItemProcessor: ItemProcessor<Advertising, Advertising>,
    @Qualifier("advertisingDeactivationItemWriter") private val advertisingItemWriter: ItemWriter<Advertising>
) {

    @Bean
    fun advertisingDeactivationStep(): Step {
        return StepBuilder("advertisingDeactivationStep", jobRepository)
            .chunk<Advertising, Advertising>(100, transactionManager)
            .reader(advertisingItemReader)
            .processor(advertisingItemProcessor)
            .writer(advertisingItemWriter)
            .build()
    }

    @Bean
    fun advertisingDeactivationJob(): Job {
        return JobBuilder("advertisingDeactivationJob", jobRepository)
            .start(advertisingDeactivationStep())
            .build()
    }
}