package com.advertising.advertising_exposure.controller

import com.advertising.advertising_exposure.controller.dto.AdvertisementReq
import com.advertising.advertising_exposure.controller.dto.AdvertisementRes
import com.advertising.advertising_exposure.domain.Advertisement
import com.advertising.advertising_exposure.service.AdvertisingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/advertising")
class AdvertisingController(private val advertisingService: AdvertisingService) {

    @PostMapping
    fun saveAdvertisingInfo(@RequestBody advertisementReq: AdvertisementReq): AdvertisementRes {
        return advertisingService.saveAdvertisementInfo(advertisementReq)
    }

    @GetMapping
    fun filterAndSortAdvertisingInfos(
        @RequestParam(required = false) minOrderPrice: Int?,
        @RequestParam(required = false) maxDeliveryFee: Int?,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): List<AdvertisementRes> {
        return advertisingService.filterAndSortAdvertisementInfos(minOrderPrice, maxDeliveryFee, sortBy, page, size)
    }
}