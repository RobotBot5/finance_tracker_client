package com.robotbot.finance_tracker_client.analytics.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.analytics.entities.AnalyticByCategoriesEntity
import com.robotbot.finance_tracker_client.analytics.entities.AnalyticsByCategoriesEntity
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import java.math.BigDecimal

internal data class AnalyticsResponse(
    @SerializedName("result") val result: List<AnalyticsDto>,
    @SerializedName("totalAmount") val totalAmount: BigDecimal,
    @SerializedName("targetCurrency") val targetCurrency: CurrencyDto
) {

    fun toEntity(): AnalyticsByCategoriesEntity = AnalyticsByCategoriesEntity(
        analytics = result.map(AnalyticsDto::toEntity),
        totalAmount = totalAmount,
        targetCurrency = targetCurrency.toEntity()
    )
}
