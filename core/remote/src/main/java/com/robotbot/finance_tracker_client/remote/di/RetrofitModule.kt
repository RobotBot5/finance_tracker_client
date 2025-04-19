package com.robotbot.finance_tracker_client.remote.di

import com.robotbot.finance_tracker_client.remote.ApiFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [PrefsModule::class])
internal object RetrofitModule {

    @Provides
    fun provideRetrofit(apiFactory: ApiFactory): Retrofit {
        return apiFactory.retrofit
    }
}