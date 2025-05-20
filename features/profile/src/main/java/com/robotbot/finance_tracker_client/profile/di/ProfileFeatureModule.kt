package com.robotbot.finance_tracker_client.profile.di

import dagger.Module

@Module(
    includes = [
        ComponentFactoryModule::class,
        ProfileDataModule::class
    ]
)
interface ProfileFeatureModule
