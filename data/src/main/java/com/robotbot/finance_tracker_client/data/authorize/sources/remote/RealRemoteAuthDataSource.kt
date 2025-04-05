package com.robotbot.finance_tracker_client.data.authorize.sources.remote

import com.robotbot.finance_tracker_client.data.authorize.entities.JwtToken
import com.robotbot.finance_tracker_client.data.authorize.sources.RemoteAuthDataSource
import com.robotbot.finance_tracker_client.data.authorize.sources.remote.base.LoginApi
import com.robotbot.finance_tracker_client.data.authorize.sources.remote.base.wrapRetrofitExceptions
import com.robotbot.finance_tracker_client.data.authorize.sources.remote.dto.LoginRequest
import com.robotbot.finance_tracker_client.data.authorize.sources.remote.dto.RegisterRequest

internal class RealRemoteAuthDataSource(
    private val loginApi: LoginApi
) : RemoteAuthDataSource {

    override suspend fun login(email: String, password: String): JwtToken = wrapRetrofitExceptions {
        JwtToken(loginApi.login(LoginRequest(email, password)).jwtToken)
    }

    override suspend fun register(email: String, password: String, firstName: String) = wrapRetrofitExceptions {
        loginApi.register(RegisterRequest(email, password, firstName))
    }
}
