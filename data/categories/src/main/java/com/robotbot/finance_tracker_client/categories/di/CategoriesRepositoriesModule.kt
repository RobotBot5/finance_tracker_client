package com.robotbot.finance_tracker_client.categories.di

import com.robotbot.finance_tracker_client.categories.CategoriesRepository
import com.robotbot.finance_tracker_client.categories.RealCategoriesRepository
import dagger.Binds
import dagger.Module

@Module
internal interface CategoriesRepositoriesModule {

    @Binds
    fun bindCategoriesRepository(impl: RealCategoriesRepository): CategoriesRepository
}