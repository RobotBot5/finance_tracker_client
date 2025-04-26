package com.robotbot.finance_tracker_client.get_info.sources.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity

internal data class CurrencyResponse(
    @SerializedName("currencies") val currencies: List<CurrencyDto>
) {

    fun toEntities(): List<CurrencyEntity> = currencies.map(CurrencyDto::toEntity)
}
