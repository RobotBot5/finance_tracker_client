package com.robotbot.finance_tracker_client.transactions.di

import dagger.Module

@Module(includes = [
    TransactionsRepositoriesModule::class,
    TransactionsSourcesModule::class
])
interface TransactionsDataModule