package com.robotbot.finance_tracker_client.analytics.sources

import com.robotbot.finance_tracker_client.analytics.entities.AnalyticsByCategoriesEntity
import com.robotbot.finance_tracker_client.analytics.sources.remote.base.AnalyticsApi
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.entities.CategoryType.EXPENSE
import com.robotbot.finance_tracker_client.remote.util.wrapRetrofitExceptions
import javax.inject.Inject

internal interface RemoteAnalyticsSource {

    suspend fun getAnalyticsByCategories(categoryType: CategoryType): AnalyticsByCategoriesEntity
}

internal class RealRemoteAnalyticsSource @Inject constructor(
    private val analyticsApi: AnalyticsApi
) : RemoteAnalyticsSource {

    override suspend fun getAnalyticsByCategories(categoryType: CategoryType): AnalyticsByCategoriesEntity = wrapRetrofitExceptions {
        analyticsApi.getAnalyticsByCategories(categoryType == EXPENSE).toEntity()
    }
}