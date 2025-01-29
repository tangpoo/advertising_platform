package com.advertising.advertising_exposure.event

import com.advertising.advertising_exposure.domain.Advertising

interface BillingEventPublisher {
    fun sendBillingEvent(advertising: Advertising)
}