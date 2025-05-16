package com.robotbot.finance_tracker_client.authorize.di

import com.robotbot.finance_tracker_client.authorize.register.di.RegisterComponentFactory
import dagger.Module

@Module(includes = [
    AuthorizeDataModule::class,
    ComponentFactoryModule::class,
    RegisterComponentFactory::class
])
interface AuthorizeFeatureModule