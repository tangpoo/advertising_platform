package com.advertising.advertising_billing.repository

import com.advertising.advertising_billing.domain.Billing
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface BillingRepository : JpaRepository<Billing, Long> {
    fun findByShopId(shopId: Long): Optional<Billing>
}