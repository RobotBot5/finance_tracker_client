package com.robotbot.finance_tracker_client.analytics.di

import com.robotbot.finance_tracker_client.analytics.AnalyticsRepository
import com.robotbot.finance_tracker_client.analytics.RealAnalyticsRepository
import dagger.Binds
import dagger.Module

@Module
internal interface AnalyticsRepositoriesModule {

    @Binds
    fun bindAnalyticsRepository(
        impl: RealAnalyticsRepository
    ): AnalyticsRepository
}