package com.robotbot.finance_tracker_client.transactions.di

import com.robotbot.finance_tracker_client.transactions.RealTransactionsRepository
import com.robotbot.finance_tracker_client.transactions.TransactionsRepository
import dagger.Binds
import dagger.Module

@Module
internal interface TransactionsRepositoriesModule {

    @Binds
    fun bindTransactionsRepository(impl: RealTransactionsRepository): TransactionsRepository
}