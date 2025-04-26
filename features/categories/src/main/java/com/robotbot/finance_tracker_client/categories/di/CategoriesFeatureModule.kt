package com.robotbot.finance_tracker_client.categories.di

import dagger.Module

@Module(includes = [
    CategoriesDataModule::class,
    ComponentFactoryModule::class
])
interface CategoriesFeatureModule