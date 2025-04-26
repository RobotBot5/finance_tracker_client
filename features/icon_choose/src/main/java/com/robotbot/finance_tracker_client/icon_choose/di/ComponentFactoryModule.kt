package com.robotbot.finance_tracker_client.icon_choose.di

import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconComponent
import com.robotbot.finance_tracker_client.icon_choose.presentation.DefaultChooseIconComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindChooseIconComponentFactory(impl: DefaultChooseIconComponent.Factory): ChooseIconComponent.Factory
}