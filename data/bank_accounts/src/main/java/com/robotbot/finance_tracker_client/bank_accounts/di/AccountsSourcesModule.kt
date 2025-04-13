package com.robotbot.finance_tracker_client.bank_accounts.di

import com.robotbot.finance_tracker_client.bank_accounts.sources.RemoteAccountsSource
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.RealRemoteAccountsSource
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.base.AccountsApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface AccountsSourcesModule {

    @Binds
    fun bindRemoteAccountsSource(
        impl: RealRemoteAccountsSource
    ): RemoteAccountsSource

    companion object {

        @Provides
        fun provideAccountsApi(retrofit: Retrofit): AccountsApi {
            return retrofit.create(AccountsApi::class.java)
        }
    }
}