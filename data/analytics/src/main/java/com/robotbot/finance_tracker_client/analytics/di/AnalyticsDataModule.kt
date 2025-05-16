package com.robotbot.finance_tracker_client.analytics.di

import dagger.Module

@Module(
    includes = [
        AnalyticsRepositoriesModule::class,
        AnalyticsSourcesModule::class
    ]
)
interface AnalyticsDataModule