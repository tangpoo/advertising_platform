package com.advertising.advertising_exposure.controller

import com.advertising.advertising_exposure.controller.dto.AdvertisingInfoReq
import com.advertising.advertising_exposure.domain.AdvertisingInfo
import com.advertising.advertising_exposure.service.AdvertisingService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/advertising")
class AdvertisingController(private val advertisingService: AdvertisingService) {

    @PostMapping
    fun saveAdvertisingInfo(@RequestBody advertisingInfoReq: AdvertisingInfoReq): AdvertisingInfo {
        return advertisingService.saveAdvertisingInfo(advertisingInfoReq)
    }
}