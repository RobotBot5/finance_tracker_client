package com.robotbot.finance_tracker_client.authorize.sources.remote.dto

import com.google.gson.annotations.SerializedName

internal data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
