package com.advertising.advertising_exposure.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class BatchTestConfig {

    @Bean
    fun jobLauncherTestUtils(jobLauncher: JobLauncher, job: Job): JobLauncherTestUtils {
        val utils = JobLauncherTestUtils()
        utils.jobLauncher = jobLauncher
        utils.job = job
        return utils
    }
}