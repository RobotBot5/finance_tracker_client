package com.robotbot.finance_tracker_client.transactions.main.di

import com.robotbot.finance_tracker_client.transactions.main.presentation.DefaultTransactionsComponent
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsComponent
import dagger.Binds
import dagger.Module

@Module
internal interface TransactionsComponentFactoryModule {

    @Binds
    fun bindTransactionsComponentFactory(impl: DefaultTransactionsComponent.Factory): TransactionsComponent.Factory
}