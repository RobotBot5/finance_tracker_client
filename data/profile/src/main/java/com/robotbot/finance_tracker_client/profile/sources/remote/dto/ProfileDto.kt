package com.robotbot.finance_tracker_client.profile.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.profile.entities.ProfileEntity

internal data class ProfileDto(
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("targetCurrency") val targetCurrency: CurrencyDto
) {

    fun toEntity(): ProfileEntity = ProfileEntity(email, firstName, targetCurrency.toEntity())
}
