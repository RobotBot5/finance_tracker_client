package com.robotbot.finance_tracker_client.categories.sources.remote.base

import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryCreateRequest
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryDto
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryUpdateRequest
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.GetCategoriesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

internal interface CategoriesApi {

    @GET("categories")
    suspend fun getCategories(): GetCategoriesResponse

    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: Long): CategoryDto

    @POST("categories")
    suspend fun addCategory(@Body categoryCreateRequest: CategoryCreateRequest)

    @PATCH("categories/{id}")
    suspend fun updateCategory(@Path("id") id: Long, @Body categoryUpdateRequest: CategoryUpdateRequest)

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Long)
}