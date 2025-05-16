package com.robotbot.finance_tracker_client.analytics

import com.robotbot.finance_tracker_client.analytics.entities.AnalyticsByCategoriesEntity
import com.robotbot.finance_tracker_client.analytics.sources.RemoteAnalyticsSource
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import javax.inject.Inject

interface AnalyticsRepository {

    suspend fun getAnalyticsByCategories(categoryType: CategoryType): AnalyticsByCategoriesEntity
}

internal class RealAnalyticsRepository @Inject constructor(
    private val remoteAnalyticsSource: RemoteAnalyticsSource
) : AnalyticsRepository {

    override suspend fun getAnalyticsByCategories(categoryType: CategoryType): AnalyticsByCategoriesEntity =
        remoteAnalyticsSource.getAnalyticsByCategories(categoryType)
}