package com.robotbot.finance_tracker_client.create_transfer.main.di

import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferComponent
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.DefaultCreateTransferComponent
import dagger.Binds
import dagger.Module

@Module
internal interface MainComponentFactoryModule {

    @Binds
    fun bindMainComponentFactory(impl: DefaultCreateTransferComponent.Factory): CreateTransferComponent.Factory
}