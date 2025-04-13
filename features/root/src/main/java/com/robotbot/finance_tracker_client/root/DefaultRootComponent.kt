package com.robotbot.finance_tracker_client.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.root.RootComponent.*
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

internal class DefaultRootComponent @AssistedInject constructor(
    private val authorizeComponentFactory: AuthorizeComponent.Factory,
    private val accountsComponentFactory: AccountsComponent.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Accounts,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(config: Config, childComponentContext: ComponentContext): Child =
        when (config) {
            is Config.Authorize -> Child.Authorize(authorizeComponent(childComponentContext))
            is Config.Accounts -> Child.Accounts(accountsComponent(childComponentContext))
        }

    private fun authorizeComponent(componentContext: ComponentContext): AuthorizeComponent =
        authorizeComponentFactory(
            onAuthSuccess = { navigation.pop() },
            componentContext = componentContext
        )

    private fun accountsComponent(componentContext: ComponentContext): AccountsComponent =
        accountsComponentFactory(
            onAuthFailed = { navigation.pushNew(Config.Authorize) },
            componentContext = componentContext
        )

    @Serializable
    private sealed interface Config {

        @Serializable
        data object Authorize : Config

        @Serializable
        data object Accounts : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}
