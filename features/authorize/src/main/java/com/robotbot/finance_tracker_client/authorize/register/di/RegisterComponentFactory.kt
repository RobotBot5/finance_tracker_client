package com.robotbot.finance_tracker_client.authorize.register.di

import com.robotbot.finance_tracker_client.authorize.register.presentation.DefaultRegisterComponent
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterComponent
import dagger.Binds
import dagger.Module

@Module
internal interface RegisterComponentFactory {

    @Binds
    fun bindRegisterComponentFactory(impl: DefaultRegisterComponent.Factory): RegisterComponent.Factory
}