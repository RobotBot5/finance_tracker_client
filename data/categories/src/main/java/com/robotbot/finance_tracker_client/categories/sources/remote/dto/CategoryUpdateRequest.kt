package com.robotbot.finance_tracker_client.categories.sources.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryUpdateRequest(
    @SerializedName("name") val name: String? = null,
    @SerializedName("iconId") val iconId: Long? = null
)
