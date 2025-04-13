package com.robotbot.finance_tracker_client.authorize.sources.remote.base

import com.robotbot.finance_tracker_client.authorize.sources.remote.dto.LoginRequest
import com.robotbot.finance_tracker_client.authorize.sources.remote.dto.LoginResponse
import com.robotbot.finance_tracker_client.authorize.sources.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

internal interface LoginApi {

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    )
}