package com.robotbot.finance_tracker_client.profile.di

import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import dagger.Module

@Module(
    includes = [
        ProfileSourcesModule::class,
        ProfileRepositoriesModule::class,
        CoreRemoteModule::class
    ]
)
interface ProfileDataModule
