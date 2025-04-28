package com.robotbot.finance_tracker_client.create_transfer.choose_account.di

import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountComponent
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.DefaultChooseAccountComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ChooseAccountComponentFactoryModule {

    @Binds
    fun bindChooseAccountComponentFactory(impl: DefaultChooseAccountComponent.Factory): ChooseAccountComponent.Factory
}