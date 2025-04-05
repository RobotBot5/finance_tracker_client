package com.robotbot.finance_tracker_client.data.authorize.sources.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken") val jwtToken: String
)
