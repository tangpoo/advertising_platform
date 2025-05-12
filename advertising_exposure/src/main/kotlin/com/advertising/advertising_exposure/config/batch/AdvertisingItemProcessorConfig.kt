package com.advertising.advertising_exposure.config.batch

import com.advertising.advertising_exposure.domain.Advertising
import org.springframework.batch.item.ItemProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdvertisingItemProcessorConfig {

    @Bean
    fun advertisingActivationItemProcessor(): ItemProcessor<Advertising, Advertising> =
        ItemProcessor { advertising ->
            advertising.activate()
            advertising
        }

    @Bean
    fun advertisingDeactivationItemProcessor(): ItemProcessor<Advertising, Advertising> =
        ItemProcessor { advertising ->
            advertising.deactivate()
            advertising
        }
}