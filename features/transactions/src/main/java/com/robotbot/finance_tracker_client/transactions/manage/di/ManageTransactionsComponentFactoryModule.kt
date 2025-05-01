package com.robotbot.finance_tracker_client.transactions.manage.di

import com.robotbot.finance_tracker_client.transactions.manage.presentation.DefaultManageTransactionsComponent
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ManageTransactionsComponentFactoryModule {

    @Binds
    fun bindManageTransactionsComponentFactory(impl: DefaultManageTransactionsComponent.Factory): ManageTransactionsComponent.Factory
}