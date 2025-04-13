package com.robotbot.finance_tracker_client.authorize.di

import dagger.Module

@Module(includes = [
    AuthorizeRepositoriesModule::class,
    AuthorizeSourcesModule::class,
])
interface AuthorizeDataModule