package com.robotbot.finance_tracker_client.analytics.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.analytics.AnalyticsRepository
import com.robotbot.finance_tracker_client.analytics.entities.AnalyticByCategoriesEntity
import com.robotbot.finance_tracker_client.analytics.entities.AnalyticsByCategoriesEntity
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsStore.Intent
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsStore.Label
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsStore.State
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsStore.State.AnalyticsState
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsStore.State.AnalyticsState.Loading
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AnalyticsStore : Store<Intent, State, Label> {

    sealed interface Intent

    data class State(val analyticsState: AnalyticsState) {

        sealed interface AnalyticsState {
            data object Loading : AnalyticsState
            data object Error : AnalyticsState
            data class Content(val analytics: AnalyticsByCategoriesEntity) : AnalyticsState
        }
    }

    sealed interface Label
}

internal class AnalyticsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val analyticsRepository: AnalyticsRepository
) {

    fun create(): AnalyticsStore =
        object : AnalyticsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AnalyticsStore",
            initialState = State(analyticsState = Loading),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class AnalyticsStateChanged(val new: AnalyticsState) : Action
    }

    private sealed interface Msg {

        data class AnalyticsStateChanged(val new: AnalyticsState) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.AnalyticsStateChanged(Loading))
                    val analytics = analyticsRepository.getAnalyticsByCategories(CategoryType.EXPENSE)
                    dispatch(Action.AnalyticsStateChanged(AnalyticsState.Content(analytics)))

            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.AnalyticsStateChanged -> {
                    dispatch(Msg.AnalyticsStateChanged(action.new))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.AnalyticsStateChanged -> copy(analyticsState = msg.new)
        }
    }
}
