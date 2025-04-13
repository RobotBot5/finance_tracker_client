package com.robotbot.finance_tracker_client.remote.token

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class AuthInterceptor @Inject constructor(
    private val prefsAuthDataSource: PrefsAuthDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val urlPath = originalRequest.url.encodedPath

        if (urlPath.contains("auth")) {
            return chain.proceed(originalRequest)
        }

        val token = prefsAuthDataSource.readToken()

        val newRequest = if (!token.token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${token.token}")
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(newRequest)
    }
}
