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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class AdvertisingBatchJobConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val advertisingItemReader: ItemReader<Advertising>,
    private val advertisingItemProcessor: ItemProcessor<Advertising, Advertising>,
    private val advertisingItemWriter: ItemWriter<Advertising>
) {

    @Bean
    fun advertisingActivationStep(): Step {
        return StepBuilder("advertisingActivationStep", jobRepository)
            .chunk<Advertising, Advertising>(100, transactionManager)
            .reader(advertisingItemReader)
            .processor(advertisingItemProcessor)
            .writer(advertisingItemWriter)
            .build()
    }

    @Bean
    fun advertisingActivationJob(): Job {
        return JobBuilder("advertisingActivationJob", jobRepository)
            .start(advertisingActivationStep())
            .build()
    }
}