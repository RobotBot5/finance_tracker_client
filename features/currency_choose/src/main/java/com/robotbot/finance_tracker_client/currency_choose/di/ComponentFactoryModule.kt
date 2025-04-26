package com.robotbot.finance_tracker_client.currency_choose.di

import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesComponent
import com.robotbot.finance_tracker_client.currency_choose.presentation.DefaultCurrenciesComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindCurrenciesComponentFactory(
        impl: DefaultCurrenciesComponent.Factory
    ): CurrenciesComponent.Factory
}