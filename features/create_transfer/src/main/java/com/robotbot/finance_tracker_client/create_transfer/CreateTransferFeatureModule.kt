package com.robotbot.finance_tracker_client.create_transfer

import com.robotbot.finance_tracker_client.create_transfer.choose_account.di.ChooseAccountComponentFactoryModule
import com.robotbot.finance_tracker_client.create_transfer.main.di.MainComponentFactoryModule
import dagger.Module

@Module(includes = [
    ChooseAccountComponentFactoryModule::class,
    MainComponentFactoryModule::class
])
interface CreateTransferFeatureModule
