package com.robotbot.finance_tracker_client.icon_choose.di

import com.robotbot.finance_tracker_client.get_info.di.GetInfoDataModule
import dagger.Module

@Module(includes = [
    GetInfoDataModule::class,
    ComponentFactoryModule::class
])
interface ChooseIconFeatureModule