package com.robotbot.finance_tracker_client.profile.sources.remote.dto

import com.google.gson.annotations.SerializedName

internal data class UpdateTargetCurrencyRequest(
    @SerializedName("currencyId") val currencyId: String
)
