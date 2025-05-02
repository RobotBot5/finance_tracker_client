package com.robotbot.finance_tracker_client.categories.main.di

import com.robotbot.finance_tracker_client.categories.main.DefaultRootCategoriesComponent
import com.robotbot.finance_tracker_client.categories.main.RootCategoriesComponent
import dagger.Binds
import dagger.Module

@Module
internal interface MainCategoriesComponentFactoryModule {

    @Binds
    fun bindMainCategoriesComponentFactory(impl: DefaultRootCategoriesComponent.Factory): RootCategoriesComponent.Factory
}
