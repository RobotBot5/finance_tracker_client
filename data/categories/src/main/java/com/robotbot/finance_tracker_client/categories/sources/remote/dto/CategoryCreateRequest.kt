package com.robotbot.finance_tracker_client.categories.sources.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryCreateRequest(
    @SerializedName("name") val name: String,
    @SerializedName("isExpense") val isExpense: Boolean,
    @SerializedName("iconId") val iconId: Long
)
