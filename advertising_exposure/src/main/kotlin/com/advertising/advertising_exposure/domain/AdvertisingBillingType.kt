package com.advertising.advertising_exposure.domain

enum class AdvertisingBillingType(
    private val paymentMethod: String
) {
    CHARGE("수수료형"),
    FLAT_RATE("정액제")
}
