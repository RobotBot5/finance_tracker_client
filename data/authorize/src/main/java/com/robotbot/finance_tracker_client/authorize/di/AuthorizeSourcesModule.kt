package com.robotbot.finance_tracker_client.authorize.di

import com.robotbot.finance_tracker_client.authorize.sources.RemoteAuthDataSource
import com.robotbot.finance_tracker_client.authorize.sources.remote.RealRemoteAuthDataSource
import com.robotbot.finance_tracker_client.authorize.sources.remote.base.LoginApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
internal interface AuthorizeSourcesModule {

    @Binds
    @Singleton
    fun bindRemoteAuthDataSource(
        remoteAuthDataSource: RealRemoteAuthDataSource
    ): RemoteAuthDataSource

    companion object {

        @Provides
        @Singleton
        fun provideLoginApi(retrofit: Retrofit): LoginApi {
            return retrofit.create(LoginApi::class.java)
        }
    }
}