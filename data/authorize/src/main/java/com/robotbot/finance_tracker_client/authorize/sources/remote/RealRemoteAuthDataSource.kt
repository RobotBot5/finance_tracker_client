package com.robotbot.finance_tracker_client.authorize.sources.remote

import com.robotbot.finance_tracker_client.authorize.sources.RemoteAuthDataSource
import com.robotbot.finance_tracker_client.authorize.sources.remote.base.LoginApi
import com.robotbot.finance_tracker_client.authorize.sources.remote.dto.LoginRequest
import com.robotbot.finance_tracker_client.authorize.sources.remote.dto.RegisterRequest
import com.robotbot.finance_tracker_client.remote.token.JwtToken
import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapper
import javax.inject.Inject

internal class RealRemoteAuthDataSource @Inject constructor(
    private val loginApi: LoginApi,
    private val wrapper: RemoteExceptionsWrapper
) : RemoteAuthDataSource {

    override suspend fun login(email: String, password: String): JwtToken =
        wrapper.wrapRetrofitExceptions {
        JwtToken(loginApi.login(LoginRequest(email, password)).jwtToken)
    }

    override suspend fun register(email: String, password: String, firstName: String) =
        wrapper.wrapRetrofitExceptions {
        loginApi.register(RegisterRequest(email, password, firstName))
    }
}
