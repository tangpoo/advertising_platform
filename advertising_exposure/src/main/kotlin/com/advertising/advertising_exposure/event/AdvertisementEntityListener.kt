package com.advertising.advertising_exposure.event

import com.advertising.advertising_exposure.domain.Advertisement

data class AdvertisementEvent(
    val advertisement: Advertisement,
    val eventType: EventType
)