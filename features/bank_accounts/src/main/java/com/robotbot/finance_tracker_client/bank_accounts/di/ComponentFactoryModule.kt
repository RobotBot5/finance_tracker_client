package com.robotbot.finance_tracker_client.bank_accounts.di

import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.DefaultAccountsComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindComponentFactory(impl: DefaultAccountsComponent.Factory): AccountsComponent.Factory
}