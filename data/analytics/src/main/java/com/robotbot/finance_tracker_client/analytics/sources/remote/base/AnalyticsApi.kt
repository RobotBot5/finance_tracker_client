package com.robotbot.finance_tracker_client.analytics.sources.remote.base

import com.robotbot.finance_tracker_client.analytics.sources.remote.dto.AnalyticsResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface AnalyticsApi {

    @GET("analytics/categories?sortOrder=desc")
    suspend fun getAnalyticsByCategories(@Query("isExpense") isExpense: Boolean): AnalyticsResponse
}