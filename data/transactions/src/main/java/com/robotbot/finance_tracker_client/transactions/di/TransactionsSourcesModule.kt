package com.robotbot.finance_tracker_client.transactions.di

import com.robotbot.finance_tracker_client.transactions.sources.RealRemoteTransactionsSource
import com.robotbot.finance_tracker_client.transactions.sources.RemoteTransactionsSource
import com.robotbot.finance_tracker_client.transactions.sources.remote.base.TransactionsApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface TransactionsSourcesModule {

    @Binds
    fun bindRemoteTransactionsSource(impl: RealRemoteTransactionsSource): RemoteTransactionsSource

    companion object {

        @Provides
        fun provideTransactionsApi(retrofit: Retrofit): TransactionsApi {
            return retrofit.create(TransactionsApi::class.java)
        }
    }
}