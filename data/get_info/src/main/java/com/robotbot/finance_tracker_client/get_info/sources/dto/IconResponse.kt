package com.robotbot.finance_tracker_client.get_info.sources.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity

internal data class IconResponse(
    @SerializedName("icons") val icons: List<IconDto>
) {

    fun toEntities(): List<IconEntity> = icons.map(IconDto::toEntity)
}
