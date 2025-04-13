package com.robotbot.finance_tracker_client.bank_accounts.di

import dagger.Module

@Module(includes = [
    AccountsDataModule::class,
    ComponentFactoryModule::class
])
interface AccountsFeatureModule