package com.robotbot.finance_tracker_client.authorize.register.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.authorize.AuthorizeDataRepository
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterStore.Intent
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterStore.Label
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface RegisterStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class EmailChanged(val newValue: String) : Intent
        data class PasswordChanged(val newValue: String) : Intent
        data class PasswordConfirmChanged(val newValue: String) : Intent
        data class FirstNameChanged(val newValue: String) : Intent
        data object RegisterButtonClicked : Intent
    }

    data class State(
        val email: String,
        val password: String,
        val passwordConfirm: String,
        val firstName: String
    )

    sealed interface Label {

        data object SuccessRegistered : Label

        data object ErrorRegistered : Label
    }
}

internal class RegisterStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val repository: AuthorizeDataRepository
) {

    fun create(): RegisterStore =
        object : RegisterStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegisterStore",
            initialState = State(
                email = "",
                password = "",
                passwordConfirm = "",
                firstName = ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class EmailChanged(val newValue: String) : Msg
        data class PasswordChanged(val newValue: String) : Msg
        data class PasswordConfirmChanged(val newValue: String) : Msg
        data class FirstNameChanged(val newValue: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.EmailChanged -> dispatch(Msg.EmailChanged(intent.newValue))
                is Intent.FirstNameChanged -> dispatch(Msg.FirstNameChanged(intent.newValue))
                is Intent.PasswordChanged -> dispatch(Msg.PasswordChanged(intent.newValue))
                is Intent.PasswordConfirmChanged -> dispatch(Msg.PasswordConfirmChanged(intent.newValue))
                Intent.RegisterButtonClicked -> {
                    scope.launch {
                        try {
                            repository.register(state().email, state().password, state().firstName)
                            publish(Label.ErrorRegistered)
                        } catch (e: Exception) {
                            publish(Label.SuccessRegistered)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.EmailChanged -> copy(email = msg.newValue)
            is Msg.FirstNameChanged -> copy(firstName = msg.newValue)
            is Msg.PasswordChanged -> copy(password = msg.newValue)
            is Msg.PasswordConfirmChanged -> copy(passwordConfirm = msg.newValue)
        }
    }
}
