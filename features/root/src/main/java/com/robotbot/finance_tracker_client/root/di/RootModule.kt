package com.robotbot.finance_tracker_client.root.di
import com.robotbot.finance_tracker_client.authorize.di.AuthorizeFeatureModule
import com.robotbot.finance_tracker_client.bank_accounts.di.AccountsFeatureModule
import dagger.Module

@Module(includes = [
    AuthorizeFeatureModule::class,
    AccountsFeatureModule::class,
    ComponentFactoryModule::class
])
interface RootModule