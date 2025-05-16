package com.robotbot.finance_tracker_client.analytics.di

import dagger.Module

@Module(
    includes = [
        AnalyticsDataModule::class,
        ComponentFactoryModule::class
    ]
)
interface AnalyticsFeatureModule