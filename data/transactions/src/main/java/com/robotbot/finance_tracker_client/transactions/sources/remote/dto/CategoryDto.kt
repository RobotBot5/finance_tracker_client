package com.robotbot.finance_tracker_client.transactions.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType.EXPENSE
import com.robotbot.finance_tracker_client.categories.entities.CategoryType.INCOME

internal data class CategoryDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("isExpense") val isExpense: Boolean,
    @SerializedName("isSystem") val isSystem: Boolean,
    @SerializedName("icon") val icon: IconDto
) {

    fun toEntity(): CategoryEntity {
        val type = if (isExpense) EXPENSE else INCOME
        return CategoryEntity(id, name, type, isSystem, icon.toEntity())
    }
}
