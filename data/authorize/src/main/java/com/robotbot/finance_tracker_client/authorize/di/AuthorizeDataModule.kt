package com.robotbot.finance_tracker_client.authorize.di

import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import dagger.Module

@Module(includes = [
    AuthorizeRepositoriesModule::class,
    AuthorizeSourcesModule::class,
    CoreRemoteModule::class
])
interface AuthorizeDataModule
