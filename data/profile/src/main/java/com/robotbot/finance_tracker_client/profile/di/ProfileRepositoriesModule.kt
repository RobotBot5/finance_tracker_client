package com.robotbot.finance_tracker_client.profile.di

import com.robotbot.finance_tracker_client.profile.ProfileRepository
import com.robotbot.finance_tracker_client.profile.RealProfileRepository
import dagger.Binds
import dagger.Module

@Module
internal interface ProfileRepositoriesModule {

    @Binds
    fun bindProfileRepository(impl: RealProfileRepository): ProfileRepository
}
