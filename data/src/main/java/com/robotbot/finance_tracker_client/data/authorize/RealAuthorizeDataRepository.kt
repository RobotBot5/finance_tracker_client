package com.robotbot.finance_tracker_client.data.authorize

import com.robotbot.finance_tracker_client.data.AuthorizeDataRepository
import com.robotbot.finance_tracker_client.data.authorize.sources.PrefsAuthDataSource
import com.robotbot.finance_tracker_client.data.authorize.sources.RemoteAuthDataSource

internal class RealAuthorizeDataRepository(
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