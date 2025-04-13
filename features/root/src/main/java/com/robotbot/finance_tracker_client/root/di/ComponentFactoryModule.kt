package com.robotbot.finance_tracker_client.root.di

import com.robotbot.finance_tracker_client.root.DefaultRootComponent
import com.robotbot.finance_tracker_client.root.RootComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindComponentFactory(impl: DefaultRootComponent.Factory): RootComponent.Factory
}