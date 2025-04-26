package com.robotbot.finance_tracker_client.categories.di

import com.robotbot.finance_tracker_client.categories.presentation.CategoriesComponent
import com.robotbot.finance_tracker_client.categories.presentation.DefaultCategoriesComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindsCategoriesComponentFactory(impl: DefaultCategoriesComponent.Factory): CategoriesComponent.Factory
}