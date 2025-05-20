package com.robotbot.finance_tracker_client.profile.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.authorize.AuthorizeDataRepository
import com.robotbot.finance_tracker_client.profile.ProfileRepository
import com.robotbot.finance_tracker_client.profile.entities.ProfileEntity
import com.robotbot.finance_tracker_client.profile.presentation.ProfileStore.Intent
import com.robotbot.finance_tracker_client.profile.presentation.ProfileStore.Label
import com.robotbot.finance_tracker_client.profile.presentation.ProfileStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object LogoutClicked : Intent

        data object TargetCurrencyClicked : Intent

        data class TargetCurrencyChanged(val newTargetCurrencyCode: String) : Intent

        data object Reload : Intent
    }

    sealed interface State {

        data object Loading : State
        data class Content(val profile: ProfileEntity) : State
        data class Error(val errorMsg: String) : State
    }

    sealed interface Label {

        data object LogoutNavigate : Label

        data class TargetCurrencyChangeNavigate(val yetSelectedTargetCurrencyCode: String) : Label
    }
}

internal class ProfileStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val profileRepository: ProfileRepository,
    private val authorizeDataRepository: AuthorizeDataRepository
) {

    private var loadingJob: Job? = null

    fun create(): ProfileStore =
        object : ProfileStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ChooseIconsStore",
            initialState = State.Loading,
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class ProfileStateChanged(val profileState: State) : Action
    }

    private sealed interface Msg {

        data class ProfileStateChanged(val profileState: State) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            loadingJob = scope.launch {
                dispatch(Action.ProfileStateChanged(State.Loading))
                try {
                    val profile = profileRepository.getProfile()
                    dispatch(Action.ProfileStateChanged(State.Content(profile)))
                } catch (e: Exception) {
                    dispatch(Action.ProfileStateChanged(State.Error(e.message ?: "Unknown error")))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.LogoutClicked -> {
                    try {
                        authorizeDataRepository.logout()
                        publish(Label.LogoutNavigate)
                    } catch (e: Exception) {
                        dispatch(Msg.ProfileStateChanged(State.Error(e.message ?: "Unknown error")))
                    }
                }

                Intent.TargetCurrencyClicked -> {
                    val state = state()
                    if (state is State.Content) {
                        publish(Label.TargetCurrencyChangeNavigate(state.profile.targetCurrency.code))
                    }
                }

                Intent.Reload -> {
                    loadingJob?.cancel()
                    loadingJob = scope.launch {
                        dispatch(Msg.ProfileStateChanged(State.Loading))
                        try {
                            val profile = profileRepository.getProfile()
                            dispatch(Msg.ProfileStateChanged(State.Content(profile)))
                        } catch (e: Exception) {
                            dispatch(
                                Msg.ProfileStateChanged(
                                    State.Error(
                                        e.message ?: "Unknown error"
                                    )
                                )
                            )
                        }
                    }
                }

                is Intent.TargetCurrencyChanged -> {
                    loadingJob?.cancel()
                    loadingJob = scope.launch {
                        dispatch(Msg.ProfileStateChanged(State.Loading))
                        try {
                            profileRepository.setTargetCurrency(intent.newTargetCurrencyCode)
                            val profile = profileRepository.getProfile()
                            dispatch(Msg.ProfileStateChanged(State.Content(profile)))
                        } catch (e: Exception) {
                            dispatch(
                                Msg.ProfileStateChanged(
                                    State.Error(
                                        e.message ?: "Unknown error"
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.ProfileStateChanged -> dispatch(Msg.ProfileStateChanged(action.profileState))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ProfileStateChanged -> msg.profileState
        }
    }
}
