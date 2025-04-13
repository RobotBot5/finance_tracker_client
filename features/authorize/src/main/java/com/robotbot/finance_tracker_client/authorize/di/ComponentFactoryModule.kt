package com.robotbot.finance_tracker_client.authorize.di

import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeComponent
import com.robotbot.finance_tracker_client.authorize.presentation.DefaultAuthorizeComponent
import dagger.Binds
import dagger.Module

@Module
internal interface ComponentFactoryModule {

    @Binds
    fun bindComponentFactory(impl: DefaultAuthorizeComponent.Factory): AuthorizeComponent.Factory
}