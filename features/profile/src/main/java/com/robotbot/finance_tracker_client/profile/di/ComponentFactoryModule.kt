package com.robotbot.finance_tracker_client.profile.di

import com.robotbot.finance_tracker_client.profile.presentation.DefaultProfileComponent
import com.robotbot.finance_tracker_client.profile.presentation.ProfileComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindComponentFactory(impl: DefaultProfileComponent.Factory): ProfileComponent.Factory
}
