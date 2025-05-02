package com.robotbot.finance_tracker_client.categories

import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.sources.RemoteCategoriesSource
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryCreateRequest
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryUpdateRequest
import javax.inject.Inject

interface CategoriesRepository {

    suspend fun getCategories(): List<CategoryEntity>

    suspend fun getCategoryById(id: Long): CategoryEntity

    suspend fun updateCategory(id: Long, categoryUpdateRequest: CategoryUpdateRequest)

    suspend fun getCategoriesByType(type: CategoryType): List<CategoryEntity>

    suspend fun createCategory(categoryCreateRequest: CategoryCreateRequest)

    suspend fun deleteCategory(id: Long)
}

internal class RealCategoriesRepository @Inject constructor(
    private val remoteSource: RemoteCategoriesSource
) : CategoriesRepository {

    override suspend fun getCategories(): List<CategoryEntity> {
        return remoteSource.getCategories()
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity {
        return remoteSource.getCategoryById(id)
    }

    override suspend fun updateCategory(id: Long, categoryUpdateRequest: CategoryUpdateRequest) {
        remoteSource.updateCategory(id, categoryUpdateRequest)
    }

    override suspend fun getCategoriesByType(type: CategoryType): List<CategoryEntity> {
        return remoteSource.getCategoriesByType(type == CategoryType.EXPENSE)
    }

    override suspend fun createCategory(categoryCreateRequest: CategoryCreateRequest) {
        remoteSource.createCategory(categoryCreateRequest)
    }

    override suspend fun deleteCategory(id: Long) {
        remoteSource.deleteCategory(id)
    }
}
