package com.robotbot.finance_tracker_client.authorize.sources.remote

import com.robotbot.finance_tracker_client.authorize.sources.RemoteAuthDataSource
import com.robotbot.finance_tracker_client.authorize.sources.remote.base.LoginApi
import com.robotbot.finance_tracker_client.remote.util.wrapRetrofitExceptions
import com.robotbot.finance_tracker_client.authorize.sources.remote.dto.LoginRequest
import com.robotbot.finance_tracker_client.authorize.sources.remote.dto.RegisterRequest
import com.robotbot.finance_tracker_client.remote.token.JwtToken
import javax.inject.Inject

internal class RealRemoteAuthDataSource @Inject constructor(
    private val loginApi: LoginApi
) : RemoteAuthDataSource {

    override suspend fun login(email: String, password: String): JwtToken = wrapRetrofitExceptions {
        JwtToken(loginApi.login(LoginRequest(email, password)).jwtToken)
    }

    override suspend fun register(email: String, password: String, firstName: String) = wrapRetrofitExceptions {
        loginApi.register(RegisterRequest(email, password, firstName))
    }
}
