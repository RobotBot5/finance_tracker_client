package com.robotbot.finance_tracker_client.categories.di

import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import dagger.Module

@Module(
    includes = [
        CategoriesRepositoriesModule::class,
        CategoriesSourcesModule::class,
        CoreRemoteModule::class
    ]
)
interface CategoriesDataModule
