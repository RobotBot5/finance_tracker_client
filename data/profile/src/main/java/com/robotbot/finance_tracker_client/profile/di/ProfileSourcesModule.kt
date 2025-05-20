package com.robotbot.finance_tracker_client.profile.di

import com.robotbot.finance_tracker_client.profile.sources.RealRemoteProfileSource
import com.robotbot.finance_tracker_client.profile.sources.RemoteProfileSource
import com.robotbot.finance_tracker_client.profile.sources.remote.base.ProfileApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface ProfileSourcesModule {

    @Binds
    fun bindRemoteProfileSource(impl: RealRemoteProfileSource): RemoteProfileSource

    companion object {

        @Provides
        fun provideProfileApi(retrofit: Retrofit): ProfileApi {
            return retrofit.create(ProfileApi::class.java)
        }
    }
}
