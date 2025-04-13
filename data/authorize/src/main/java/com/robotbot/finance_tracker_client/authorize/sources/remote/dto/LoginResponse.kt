package com.robotbot.finance_tracker_client.authorize.sources.remote.dto

import com.google.gson.annotations.SerializedName

internal data class LoginResponse(
    @SerializedName("accessToken") val jwtToken: String
)
