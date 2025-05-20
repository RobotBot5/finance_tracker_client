package com.robotbot.finance_tracker_client.analytics.root.di

import com.robotbot.finance_tracker_client.analytics.root.DefaultRootAnalyticsComponent
import com.robotbot.finance_tracker_client.analytics.root.RootAnalyticsComponent
import dagger.Binds
import dagger.Module

@Module
internal interface RootAnalyticsComponentFactoryModule {

    @Binds
    fun bindRootAnalyticsComponentFactory(impl: DefaultRootAnalyticsComponent.Factory): RootAnalyticsComponent.Factory
}
