package com.robotbot.finance_tracker_client.remote.di

import dagger.Module

@Module(includes = [
    PrefsModule::class,
    RetrofitModule::class,
    CoilModule::class
])
interface CoreRemoteModule