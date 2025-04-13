package com.robotbot.finance_tracker_client.authorize.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthorizeComponent {

    val model: StateFlow<AuthorizeStore.State>

    val events: SharedFlow<Events>

    fun onClickSignIn()

    fun onChangeEmail(email: String)

    fun onChangePassword(password: String)

    sealed interface Events {
        data class AuthError(val msg: String) : Events

    }

    fun interface Factory {
        operator fun invoke(
            onAuthSuccess: () -> Unit,
            componentContext: ComponentContext
        ): AuthorizeComponent
    }
}
