package com.robotbot.finance_tracker_client.manage_categories.di

import com.robotbot.finance_tracker_client.manage_categories.presentation.DefaultManageCategoriesComponent
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindManageCategoriesComponentFactory(impl: DefaultManageCategoriesComponent.Factory): ManageCategoriesComponent.Factory
}