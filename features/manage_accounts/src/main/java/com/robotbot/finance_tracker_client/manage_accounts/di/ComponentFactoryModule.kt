package com.robotbot.finance_tracker_client.manage_accounts.di

import com.robotbot.finance_tracker_client.manage_accounts.presentation.DefaultManageAccountsComponent
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindComponentFactory(impl: DefaultManageAccountsComponent.Factory): ManageAccountsComponent.Factory
}