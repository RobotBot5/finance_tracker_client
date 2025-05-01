package com.robotbot.finance_tracker_client.transactions.category_choose.di

import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseComponent
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.DefaultCategoryChooseComponent
import dagger.Binds
import dagger.Module

@Module
internal interface CategoryChooseComponentFactoryModule {

    @Binds
    fun bindCategoryChooseComponentFactory(impl: DefaultCategoryChooseComponent.Factory): CategoryChooseComponent.Factory
}