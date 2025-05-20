package com.robotbot.finance_tracker_client.bank_accounts.di

import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import dagger.Module

@Module(includes = [
    AccountsRepositoriesModule::class,
    AccountsSourcesModule::class,
    CoreRemoteModule::class
])
interface AccountsDataModule
