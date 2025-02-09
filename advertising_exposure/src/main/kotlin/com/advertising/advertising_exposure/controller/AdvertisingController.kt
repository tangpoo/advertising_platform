package com.advertising.advertising_exposure.controller

import com.advertising.advertising_exposure.controller.dto.AdvertisementReq
import com.advertising.advertising_exposure.controller.dto.AdvertisementRes
import com.advertising.advertising_exposure.controller.dto.AdvertisingReq
import com.advertising.advertising_exposure.controller.dto.AdvertisingRes
import com.advertising.advertising_exposure.service.AdvertisingService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/advertising")
class AdvertisingController(private val advertisingService: AdvertisingService) {

    @PostMapping
    fun saveAdvertisingInfo(@RequestBody advertisementReq: AdvertisementReq): AdvertisementRes =
        advertisingService.saveAdvertisementInfo(advertisementReq)

    @GetMapping
    fun filterAndSortAdvertisingInfos(
        @RequestParam(required = false) minOrderPrice: Int?,
        @RequestParam(required = false) maxDeliveryFee: Int?,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): List<AdvertisementRes> = advertisingService.filterAndSortAdvertisementInfos(
        minOrderPrice,
        maxDeliveryFee,
        sortBy,
        page,
        size
    )

    @PostMapping("/posting")
    fun postAdvertisements(
        @RequestBody advertisingReq: AdvertisingReq
    ): AdvertisingRes = advertisingService.postAdvertisement(advertisingReq)
}