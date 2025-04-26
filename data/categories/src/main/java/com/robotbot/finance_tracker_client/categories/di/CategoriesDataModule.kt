package com.robotbot.finance_tracker_client.categories.di

import dagger.Module

@Module(includes = [
    CategoriesRepositoriesModule::class,
    CategoriesSourcesModule::class
])
interface CategoriesDataModule