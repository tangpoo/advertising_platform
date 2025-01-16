package com.advertising.advertising_exposure.controller.dto

data class AdvertisingInfoReq(
    val shopId: Long,
    val image: String?,
    val description: String,
    val region: String
)
