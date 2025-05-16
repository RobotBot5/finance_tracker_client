package com.robotbot.finance_tracker_client.authorize.register.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface RegisterComponent {

    val model: StateFlow<RegisterStore.State>

    val events: SharedFlow<Events>

    fun onChangeEmail(email: String)

    fun onChangeFirstName(firstName: String)

    fun onChangePassword(password: String)

    fun onChangeConfirmPassword(confirmPassword: String)

    fun onRegisterClicked()

    sealed interface Events {

        data object ErrorToast : Events
    }

    fun interface Factory {
        operator fun invoke(
            onRegisterSuccess: () -> Unit,
            componentContext: ComponentContext
        ): RegisterComponent
    }

}