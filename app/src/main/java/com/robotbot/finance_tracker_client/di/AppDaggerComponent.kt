package com.robotbot.finance_tracker_client.di

import android.app.Application
import com.robotbot.finance_tracker_client.dependencies.KotlinMviModule
import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import com.robotbot.finance_tracker_client.root.RootComponent
import com.robotbot.finance_tracker_client.root.di.RootModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RootModule::class,
        KotlinMviModule::class,
        CoreRemoteModule::class
    ]
)
interface AppDaggerComponent {

    val rootComponentFactory: RootComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): AppDaggerComponent
    }
}