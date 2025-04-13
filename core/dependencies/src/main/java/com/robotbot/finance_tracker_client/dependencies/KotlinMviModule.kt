package com.robotbot.finance_tracker_client.dependencies

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
interface KotlinMviModule {
    companion object {
        @Provides
        fun provideStoreFactory(): StoreFactory {
            return DefaultStoreFactory()
        }
    }
}