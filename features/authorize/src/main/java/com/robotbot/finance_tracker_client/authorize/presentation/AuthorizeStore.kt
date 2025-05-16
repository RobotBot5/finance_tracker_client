package com.robotbot.finance_tracker_client.authorize.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeStore.Intent
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeStore.Label
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeStore.State
import com.robotbot.finance_tracker_client.authorize.AuthorizeDataRepository
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeStore.State.AuthState
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AuthorizeStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeEmail(val email: String) : Intent

        data class ChangePassword(val password: String) : Intent

        data object ClickSignIn : Intent

        data object OnRegisterClicked : Intent
    }

    data class State(
        val email: String,
        val password: String,
        val authState: AuthState
    ) {

        sealed interface AuthState {

            data object Idle : AuthState
            data object Loading : AuthState
        }
    }

    sealed interface Label {

        data class ErrorMsg(val errorMsg: String) : Label

        data object AuthSuccess : Label

        data object OnRegisterNavigate : Label
    }
}

internal class AuthorizeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val authorizeDataRepository: AuthorizeDataRepository
) {

    fun create(): AuthorizeStore =
        object : AuthorizeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AuthorizeStore",
            initialState = State(
                email = "",
                password = "",
                authState = AuthState.Idle
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class AuthChange(val authState: AuthState) : Msg

        data class ChangeEmail(val email: String) : Msg

        data class ChangePassword(val password: String) : Msg

        data class SignInError(val errorMsg: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeEmail -> dispatch(Msg.ChangeEmail(intent.email))
                is Intent.ChangePassword -> dispatch(Msg.ChangePassword(intent.password))
                is Intent.ClickSignIn -> {
                    dispatch(Msg.AuthChange(AuthState.Loading))
                    scope.launch {
                        try {
                            authorizeDataRepository.login(state().email, state().password)
                            publish(Label.AuthSuccess)
                        } catch (e: Exception) {
                            publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                        } finally {
                            dispatch(Msg.AuthChange(AuthState.Idle))
                        }
                    }
                }
                is Intent.OnRegisterClicked -> publish(Label.OnRegisterNavigate)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeEmail -> copy(email = msg.email)
            is Msg.ChangePassword -> copy(password = msg.password)
            is Msg.SignInError -> copy(email = "", password = "")
            is Msg.AuthChange -> copy(authState = msg.authState)
        }
    }
}
