package com.robotbot.finance_tracker_client.categories.sources

import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.sources.remote.base.CategoriesApi
import com.robotbot.finance_tracker_client.remote.util.wrapRetrofitExceptions
import javax.inject.Inject

internal interface RemoteCategoriesSource {

    suspend fun getCategories(): List<CategoryEntity>
}

internal class RealRemoteCategoriesSource @Inject constructor(
    private val api: CategoriesApi
) : RemoteCategoriesSource {

    override suspend fun getCategories(): List<CategoryEntity> = wrapRetrofitExceptions {
        api.getCategories().toEntities()
    }
}