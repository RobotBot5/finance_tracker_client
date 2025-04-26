package com.robotbot.finance_tracker_client.categories.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity

internal data class GetCategoriesResponse(
    @SerializedName("categories") val categories: List<CategoryDto>
) {

    fun toEntities(): List<CategoryEntity> = categories.map(CategoryDto::toEntity)
}
