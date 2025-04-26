package com.robotbot.finance_tracker_client.get_info.di

import dagger.Module

@Module(includes = [
    GetInfoSourcesModule::class,
    GetInfoRepositoryModule::class
])
interface GetInfoDataModule