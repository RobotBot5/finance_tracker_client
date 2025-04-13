package com.robotbot.finance_tracker_client.authorize.di

import com.robotbot.finance_tracker_client.authorize.AuthorizeDataRepository
import com.robotbot.finance_tracker_client.authorize.RealAuthorizeDataRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal interface AuthorizeRepositoriesModule {

    @Binds
    @Singleton
    fun bindAuthorizeRepository(
        authorizeRepository: RealAuthorizeDataRepository
    ): AuthorizeDataRepository
}