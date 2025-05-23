package com.robotbot.finance_tracker_client.authorize.sources

import com.robotbot.finance_tracker_client.remote.token.JwtToken

internal interface RemoteAuthDataSource {

    suspend fun login(email: String, password: String): JwtToken

    suspend fun register(
        email: String,
        password: String,
        firstName: String
    )
}