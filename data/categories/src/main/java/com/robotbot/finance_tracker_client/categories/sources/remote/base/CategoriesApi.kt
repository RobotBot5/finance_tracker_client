package com.robotbot.finance_tracker_client.categories.sources.remote.base

import com.robotbot.finance_tracker_client.categories.sources.remote.dto.GetCategoriesResponse
import retrofit2.http.GET

internal interface CategoriesApi {

    @GET("categories")
    suspend fun getCategories(): GetCategoriesResponse
}