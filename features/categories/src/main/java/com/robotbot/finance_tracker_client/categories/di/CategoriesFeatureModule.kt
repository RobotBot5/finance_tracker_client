package com.robotbot.finance_tracker_client.categories.di

import com.robotbot.finance_tracker_client.categories.main.di.MainCategoriesComponentFactoryModule
import dagger.Module

@Module(includes = [
    CategoriesDataModule::class,
    ComponentFactoryModule::class,
    MainCategoriesComponentFactoryModule::class
])
interface CategoriesFeatureModule
