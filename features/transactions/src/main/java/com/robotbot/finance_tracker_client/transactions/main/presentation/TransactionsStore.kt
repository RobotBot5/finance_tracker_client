package com.robotbot.finance_tracker_client.transactions.main.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.transactions.TransactionsRepository
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore.Intent
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore.Label
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore.State
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore.State.TransactionsState
import kotlinx.coroutines.launch
import javax.inject.Inject

interface TransactionsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object OnCreateTransactionClicked : Intent

        data class OnTransactionClicked(val transactionId: Long) : Intent
    }

    data class State(
        val transactionsState: TransactionsState
    ) {

        sealed interface TransactionsState {

            data object Initial : TransactionsState
            data object Loading : TransactionsState
            data class Error(val errorMsg: String) : TransactionsState
            data class Content(val transactions: List<TransactionEntity>) : TransactionsState
        }
    }

    sealed interface Label {

        data class OnManageTransactionNavigate(val transactionId: Long?) : Label
    }
}

internal class TransactionsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val transactionsRepository: TransactionsRepository
) {

    fun create(): TransactionsStore =
        object : TransactionsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TransactionsStore",
            initialState = State(transactionsState = TransactionsState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class ChangeTransactionsState(val transactionsState: TransactionsState) : Action
    }

    private sealed interface Msg {

        data class ChangeTransactionsState(val transactionsState: TransactionsState) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                try {
                    dispatch(Action.ChangeTransactionsState(TransactionsState.Loading))
                    val transactions = transactionsRepository.getTransactions()
                    dispatch(Action.ChangeTransactionsState(TransactionsState.Content(transactions)))
                } catch (e: Exception) {
                    dispatch(Action.ChangeTransactionsState(TransactionsState.Error(e.message ?: "Unknown message")))
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.ChangeTransactionsState -> dispatch(Msg.ChangeTransactionsState(action.transactionsState))
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.OnCreateTransactionClicked -> publish(Label.OnManageTransactionNavigate(null))
                is Intent.OnTransactionClicked -> publish(Label.OnManageTransactionNavigate(intent.transactionId))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeTransactionsState -> copy(transactionsState = msg.transactionsState)
        }
    }
}
