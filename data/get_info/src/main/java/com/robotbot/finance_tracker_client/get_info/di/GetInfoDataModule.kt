package com.robotbot.finance_tracker_client.get_info.di

import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import dagger.Module

@Module(includes = [
    GetInfoSourcesModule::class,
    GetInfoRepositoryModule::class,
    CoreRemoteModule::class
])
interface GetInfoDataModule
