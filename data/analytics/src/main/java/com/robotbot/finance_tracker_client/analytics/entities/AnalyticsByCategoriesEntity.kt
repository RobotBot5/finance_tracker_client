package com.robotbot.finance_tracker_client.analytics.entities

import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import java.math.BigDecimal

data class AnalyticsByCategoriesEntity(
    val analytics: List<AnalyticByCategoriesEntity>,
    val totalAmount: BigDecimal,
    val targetCurrency: CurrencyEntity
)
