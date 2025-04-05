package com.robotbot.finance_tracker_client.data.authorize.di

import com.robotbot.finance_tracker_client.data.AuthorizeDataRepository
import com.robotbot.finance_tracker_client.data.authorize.RealAuthorizeDataRepository
import com.robotbot.finance_tracker_client.data.authorize.sources.PrefsAuthDataSource
import com.robotbot.finance_tracker_client.data.authorize.sources.RemoteAuthDataSource
import com.robotbot.finance_tracker_client.data.authorize.sources.prefs.RealPrefsAuthDataSource
import com.robotbot.finance_tracker_client.data.authorize.sources.remote.RealRemoteAuthDataSource
import com.robotbot.finance_tracker_client.data.authorize.sources.remote.base.ApiFactory
import com.robotbot.finance_tracker_client.data.authorize.sources.remote.base.LoginApi

interface AuthorizeModule {

    val authorizeDataRepository: AuthorizeDataRepository
}

fun AuthorizeModule(deps: AuthorizeDependencies) = object : AuthorizeModule {

    override val authorizeDataRepository: AuthorizeDataRepository
        get() = RealAuthorizeDataRepository(prefsAuthDataSource, remoteAuthDataSource)

    val prefsAuthDataSource: PrefsAuthDataSource by lazy {
        RealPrefsAuthDataSource(deps.application)
    }

    val remoteAuthDataSource: RemoteAuthDataSource
        get() = RealRemoteAuthDataSource(loginApi)

    val loginApi: LoginApi
        get() = ApiFactory.loginApi
}