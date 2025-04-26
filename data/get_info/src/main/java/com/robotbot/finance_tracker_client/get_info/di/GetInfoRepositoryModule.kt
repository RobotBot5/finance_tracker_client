package com.robotbot.finance_tracker_client.get_info.di

import com.robotbot.finance_tracker_client.get_info.GetInfoRepository
import com.robotbot.finance_tracker_client.get_info.RealGetInfoRepository
import dagger.Binds
import dagger.Module

@Module
internal interface GetInfoRepositoryModule {

    @Binds
    fun bindGetInfoRepository(impl: RealGetInfoRepository): GetInfoRepository
}