package com.robotbot.finance_tracker_client.authorize.di

import dagger.Module

@Module(includes = [
    AuthorizeDataModule::class,
    ComponentFactoryModule::class
])
interface AuthorizeFeatureModule