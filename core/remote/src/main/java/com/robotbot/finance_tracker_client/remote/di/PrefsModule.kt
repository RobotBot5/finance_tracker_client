package com.robotbot.finance_tracker_client.remote.di

import com.robotbot.finance_tracker_client.remote.token.PrefsAuthDataSource
import com.robotbot.finance_tracker_client.remote.token.RealPrefsAuthDataSource
import dagger.Binds
import dagger.Module

@Module
internal interface PrefsModule {

    @Binds
    fun bindPrefsAuthDataSource(
        impl: RealPrefsAuthDataSource
    ): PrefsAuthDataSource
}