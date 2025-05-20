package com.robotbot.finance_tracker_client.currency_choose.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesStore.Intent
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesStore.Label
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesStore.State
import com.robotbot.finance_tracker_client.get_info.GetInfoRepository
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CurrenciesStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class SelectCurrency(val currencyCode: String) : Intent
    }

    data class State(
        val isLoading: Boolean,
        val currenciesList: List<CurrencyEntity>,
        val selectedCurrencyCode: String?
    )

    sealed interface Label {

        data class CurrencySelected(val currencyCode: String) : Label
    }
}

internal class CurrenciesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getInfoRepository: GetInfoRepository
) {

    fun create(selectedCurrencyCode: String?): CurrenciesStore =
        object : CurrenciesStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CurrenciesStore",
            initialState = State(
                isLoading = true,
                currenciesList = listOf(),
                selectedCurrencyCode = selectedCurrencyCode
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class CurrenciesLoading(val isLoading: Boolean) : Action
        data class CurrenciesLoaded(val currencies: List<CurrencyEntity>) : Action
        data class CurrenciesError(val errorMsg: String) : Action
    }

    private sealed interface Msg {

        data class CurrenciesLoading(val isLoading: Boolean) : Msg
        data class CurrenciesLoaded(val currencies: List<CurrencyEntity>) : Msg
        data class CurrenciesError(val errorMsg: String) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.CurrenciesLoading(true))
                try {
                    val currenciesList = getInfoRepository.getCurrenciesList()
                    dispatch(Action.CurrenciesLoaded(currenciesList))
                } catch (e: Exception) {
                    dispatch(Action.CurrenciesError(e.message ?: "Unknown error"))
                } finally {
                    dispatch(Action.CurrenciesLoading(false))
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.SelectCurrency -> publish(Label.CurrencySelected(intent.currencyCode))
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.CurrenciesError -> dispatch(Msg.CurrenciesError(action.errorMsg))
                is Action.CurrenciesLoaded -> dispatch(Msg.CurrenciesLoaded(action.currencies))
                is Action.CurrenciesLoading -> dispatch(Msg.CurrenciesLoading(action.isLoading))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.CurrenciesError -> copy()
            is Msg.CurrenciesLoaded -> copy(currenciesList = msg.currencies)
            is Msg.CurrenciesLoading -> copy(isLoading = msg.isLoading)
        }
    }
}
