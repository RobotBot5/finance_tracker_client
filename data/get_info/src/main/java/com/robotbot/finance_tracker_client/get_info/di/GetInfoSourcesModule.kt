package com.robotbot.finance_tracker_client.get_info.di

import com.robotbot.finance_tracker_client.get_info.sources.RealRemoteGetInfoSource
import com.robotbot.finance_tracker_client.get_info.sources.RemoteGetInfoSource
import com.robotbot.finance_tracker_client.get_info.sources.base.InfoApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface GetInfoSourcesModule {

    @Binds
    fun bindCurrencySource(impl: RealRemoteGetInfoSource): RemoteGetInfoSource

    companion object {

        @Provides
        fun provideCurrencyApi(retrofit: Retrofit): InfoApi {
            return retrofit.create(InfoApi::class.java)
        }
    }
}