package com.advertising.advertising_exposure.config.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AdvertisingBatchScheduler(
    private val jobLauncher: JobLauncher,
    private val advertisingActivationJob: Job
) {

    @Scheduled(cron = "0 * * * * ?")
    fun runAdvertisingActivationJob() {
        val jobParameter = JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())
            .toJobParameters()

        jobLauncher.run(advertisingActivationJob, jobParameter)
    }
}