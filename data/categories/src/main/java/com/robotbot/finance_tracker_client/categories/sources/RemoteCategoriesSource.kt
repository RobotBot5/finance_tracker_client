package com.robotbot.finance_tracker_client.categories.sources

import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.sources.remote.base.CategoriesApi
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryCreateRequest
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryUpdateRequest
import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapper
import javax.inject.Inject

internal interface RemoteCategoriesSource {

    suspend fun getCategories(): List<CategoryEntity>

    suspend fun getCategoryById(id: Long): CategoryEntity

    suspend fun updateCategory(id: Long, categoryUpdateRequest: CategoryUpdateRequest)

    suspend fun createCategory(categoryCreateRequest: CategoryCreateRequest)

    suspend fun getCategoriesByType(isExpense: Boolean): List<CategoryEntity>

    suspend fun deleteCategory(id: Long)
}

internal class RealRemoteCategoriesSource @Inject constructor(
    private val api: CategoriesApi,
    private val wrapper: RemoteExceptionsWrapper
) : RemoteCategoriesSource {

    override suspend fun getCategories(): List<CategoryEntity> = wrapper.wrapRetrofitExceptions {
        api.getCategories().toEntities()
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity =
        wrapper.wrapRetrofitExceptions {
        api.getCategoryById(id).toEntity()
    }

    override suspend fun updateCategory(id: Long, categoryUpdateRequest: CategoryUpdateRequest) =
        wrapper.wrapRetrofitExceptions {
        api.updateCategory(id, categoryUpdateRequest)
    }

    override suspend fun createCategory(categoryCreateRequest: CategoryCreateRequest) =
        wrapper.wrapRetrofitExceptions {
        api.addCategory(categoryCreateRequest)
    }

    override suspend fun getCategoriesByType(isExpense: Boolean): List<CategoryEntity> =
        wrapper.wrapRetrofitExceptions {
            api.getCategoriesByType(isExpense).toEntities()
        }

    override suspend fun deleteCategory(id: Long) = wrapper.wrapRetrofitExceptions {
        api.deleteCategory(id)
    }
}
