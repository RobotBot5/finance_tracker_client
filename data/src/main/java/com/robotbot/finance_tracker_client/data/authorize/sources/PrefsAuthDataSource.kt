package com.robotbot.finance_tracker_client.data.authorize.sources

import com.robotbot.finance_tracker_client.data.authorize.entities.JwtToken

internal interface PrefsAuthDataSource {

    fun saveToken(jwtToken: JwtToken)

    fun readToken(): JwtToken?

    fun clearToken()
}