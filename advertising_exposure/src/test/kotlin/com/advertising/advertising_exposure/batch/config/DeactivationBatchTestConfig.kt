package com.advertising.advertising_exposure.batch.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class DeactivationBatchTestConfig {

    @Bean
    fun jobLauncherTestUtils(@Qualifier("advertisingDeactivationJob") job: Job, jobLauncher: JobLauncher): JobLauncherTestUtils {
        val utils = JobLauncherTestUtils()
        utils.jobLauncher = jobLauncher
        utils.job = job
        return utils
    }
}