package com.robotbot.finance_tracker_client.remote.di

import com.robotbot.finance_tracker_client.remote.token.PrefsAuthDataSource
import com.robotbot.finance_tracker_client.remote.token.RealPrefsAuthDataSource
import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapper
import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapperImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal interface PrefsModule {

    @Binds
    @Singleton
    fun bindPrefsAuthDataSource(
        impl: RealPrefsAuthDataSource
    ): PrefsAuthDataSource

    @Binds
    @Singleton
    fun bindRemoteExceptionsWrapper(
        impl: RemoteExceptionsWrapperImpl
    ): RemoteExceptionsWrapper
}
