package com.robotbot.finance_tracker_client.analytics.di

import com.robotbot.finance_tracker_client.analytics.sources.RealRemoteAnalyticsSource
import com.robotbot.finance_tracker_client.analytics.sources.RemoteAnalyticsSource
import com.robotbot.finance_tracker_client.analytics.sources.remote.base.AnalyticsApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface AnalyticsSourcesModule {

    @Binds
    fun bindRemoteAnalyticsSource(
        impl: RealRemoteAnalyticsSource
    ): RemoteAnalyticsSource

    companion object {

        @Provides
        fun provideAnalyticsApi(retrofit: Retrofit): AnalyticsApi {
            return retrofit.create(AnalyticsApi::class.java)
        }
    }
}