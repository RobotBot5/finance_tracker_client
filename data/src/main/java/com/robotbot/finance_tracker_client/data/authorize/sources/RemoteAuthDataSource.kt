package com.robotbot.finance_tracker_client.data.authorize.sources

import com.robotbot.finance_tracker_client.data.authorize.entities.JwtToken

internal interface RemoteAuthDataSource {

    suspend fun login(email: String, password: String): JwtToken

    suspend fun register(
        email: String,
        password: String,
        firstName: String
    )
}