package com.robotbot.finance_tracker_client.analytics.di

import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import dagger.Module

@Module(
    includes = [
        AnalyticsRepositoriesModule::class,
        AnalyticsSourcesModule::class,
        CoreRemoteModule::class
    ]
)
interface AnalyticsDataModule
