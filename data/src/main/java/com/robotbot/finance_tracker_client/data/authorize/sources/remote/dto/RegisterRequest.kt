package com.robotbot.finance_tracker_client.data.authorize.sources.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("firstName") val firstName: String
)
