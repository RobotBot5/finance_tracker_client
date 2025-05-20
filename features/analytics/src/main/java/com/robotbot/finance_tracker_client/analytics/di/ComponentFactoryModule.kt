package com.robotbot.finance_tracker_client.analytics.di

import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsComponent
import com.robotbot.finance_tracker_client.analytics.presentation.DefaultAnalyticsComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindAnalyticsComponentFactory(
        factory: DefaultAnalyticsComponent.Factory
    ): AnalyticsComponent.Factory
}
