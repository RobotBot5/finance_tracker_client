package com.robotbot.finance_tracker_client.transactions.di

import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import dagger.Module

@Module(includes = [
    TransactionsRepositoriesModule::class,
    TransactionsSourcesModule::class,
    CoreRemoteModule::class
])
interface TransactionsDataModule
