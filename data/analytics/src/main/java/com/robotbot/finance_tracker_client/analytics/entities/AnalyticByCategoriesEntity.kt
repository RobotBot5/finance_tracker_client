package com.robotbot.finance_tracker_client.analytics.entities

import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import java.math.BigDecimal

data class AnalyticByCategoriesEntity(
    val category: CategoryEntity,
    val totalAmount: BigDecimal,
    val percentage: Int
)