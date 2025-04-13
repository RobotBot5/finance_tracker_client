package com.robotbot.finance_tracker_client.bank_accounts.di

import dagger.Module

@Module(includes = [
    AccountsRepositoriesModule::class,
    AccountsSourcesModule::class,
])
interface AccountsDataModule