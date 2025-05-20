package com.robotbot.finance_tracker_client.profile.sources.remote.base

import com.robotbot.finance_tracker_client.profile.sources.remote.dto.ProfileDto
import com.robotbot.finance_tracker_client.profile.sources.remote.dto.UpdateTargetCurrencyRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

internal interface ProfileApi {

    @GET("profile")
    suspend fun getProfile(): ProfileDto

    @PATCH("profile/target-currency")
    suspend fun updateTargetCurrency(@Body targetCurrency: UpdateTargetCurrencyRequest)
}
