package com.robotbot.finance_tracker_client.profile.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity

internal data class CurrencyDto(
    @SerializedName("code") val code: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String
) {

    fun toEntity() = CurrencyEntity(
        code = code,
        symbol = symbol,
        name = name
    )
}
