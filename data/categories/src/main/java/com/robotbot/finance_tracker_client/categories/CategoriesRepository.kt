package com.robotbot.finance_tracker_client.categories

import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.sources.RemoteCategoriesSource
import javax.inject.Inject

interface CategoriesRepository {

    suspend fun getCategories(): List<CategoryEntity>
}

internal class RealCategoriesRepository @Inject constructor(
    private val remoteSource: RemoteCategoriesSource
) : CategoriesRepository {

    override suspend fun getCategories(): List<CategoryEntity> {
        return remoteSource.getCategories()
    }
}