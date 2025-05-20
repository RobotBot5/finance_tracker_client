package com.robotbot.finance_tracker_client.analytics.di

import com.robotbot.finance_tracker_client.analytics.root.di.RootAnalyticsComponentFactoryModule
import dagger.Module

@Module(
    includes = [
        AnalyticsDataModule::class,
        ComponentFactoryModule::class,
        RootAnalyticsComponentFactoryModule::class
    ]
)
interface AnalyticsFeatureModule
