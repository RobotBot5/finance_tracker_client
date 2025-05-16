package com.robotbot.finance_tracker_client.analytics.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.analytics.entities.AnalyticByCategoriesEntity
import java.math.BigDecimal

internal data class AnalyticsDto(
    @SerializedName("category") val category: CategoryDto,
    @SerializedName("totalAmount") val totalAmount: BigDecimal,
    @SerializedName("percentage") val percentage: Int
) {

    fun toEntity(): AnalyticByCategoriesEntity =
        AnalyticByCategoriesEntity(category.toEntity(), totalAmount, percentage)
}
