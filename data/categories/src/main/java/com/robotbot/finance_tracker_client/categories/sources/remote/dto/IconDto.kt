package com.robotbot.finance_tracker_client.categories.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity

internal data class IconDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("path") val path: String
) {

    fun toEntity() = IconEntity(id, name, path)
}
