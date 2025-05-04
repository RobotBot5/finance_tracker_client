package com.robotbot.finance_tracker_client.transactions.root.di

import com.robotbot.finance_tracker_client.transactions.root.DefaultRootTransactionsComponent
import com.robotbot.finance_tracker_client.transactions.root.RootTransactionsComponent
import dagger.Binds
import dagger.Module

@Module
internal interface RootTransactionComponentFactoryModule {

    @Binds
    fun bindRootTransactionComponentFactory(impl: DefaultRootTransactionsComponent.Factory): RootTransactionsComponent.Factory
}
