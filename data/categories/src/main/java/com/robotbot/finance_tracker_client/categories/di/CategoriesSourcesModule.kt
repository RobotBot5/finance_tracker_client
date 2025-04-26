package com.robotbot.finance_tracker_client.categories.di

import com.robotbot.finance_tracker_client.categories.sources.RealRemoteCategoriesSource
import com.robotbot.finance_tracker_client.categories.sources.RemoteCategoriesSource
import com.robotbot.finance_tracker_client.categories.sources.remote.base.CategoriesApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface CategoriesSourcesModule {

    @Binds
    fun bindRemoteCategoriesSource(impl: RealRemoteCategoriesSource): RemoteCategoriesSource

    companion object {

        @Provides
        fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi {
            return retrofit.create(CategoriesApi::class.java)
        }
    }
}