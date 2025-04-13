package com.robotbot.finance_tracker_client.authorize

import com.robotbot.finance_tracker_client.remote.token.PrefsAuthDataSource
import com.robotbot.finance_tracker_client.authorize.sources.RemoteAuthDataSource
import javax.inject.Inject

internal class RealAuthorizeDataRepository @Inject constructor(
    private val prefsAuthDataSource: PrefsAuthDataSource,
    private val remoteAuthDataSource: RemoteAuthDataSource
) : AuthorizeDataRepository {

    override suspend fun login(email: String, password: String) {
        val token = remoteAuthDataSource.login(email, password)
        prefsAuthDataSource.saveToken(token)
    }

    override fun logout() {
        prefsAuthDataSource.clearToken()
    }

    override suspend fun register(email: String, password: String, firstName: String) {
        remoteAuthDataSource.register(email, password, firstName)
    }
}