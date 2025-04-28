package com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountStore.Intent
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountStore.Label
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ChooseAccountStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class OnAccountClicked(val id: Long) : Intent
    }

    data class State(
        val yetSelectedAccountId: Long?,
        val accounts: List<AccountEntity>,
        val isLoading: Boolean
    )

    sealed interface Label {

        data class OnAccountSelected(val id: Long) : Label
    }
}

internal class ChooseAccountStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val bankAccountsRepository: BankAccountsRepository
) {

    fun create(yetSelectedAccountId: Long?): ChooseAccountStore =
        object : ChooseAccountStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ChooseAccountStore",
            initialState = State(
                yetSelectedAccountId = yetSelectedAccountId,
                accounts = listOf(),
                isLoading = true
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class AccountsLoadingChanged(val isLoading: Boolean) : Action

        data class AccountsLoadingError(val errorMsg: String) : Action

        data class AccountLoaded(val accounts: List<AccountEntity>) : Action
    }

    private sealed interface Msg {

        data class LoadingStatusChanged(val isLoading: Boolean) : Msg

        data class AccountLoaded(val accounts: List<AccountEntity>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.AccountsLoadingChanged(true))
                try {
                    val accounts = bankAccountsRepository.getAccounts()
                    dispatch(Action.AccountLoaded(accounts))
                } catch (e: Exception) {
                    dispatch(Action.AccountsLoadingError(e.message ?: "Unknown error"))
                } finally {
                    dispatch(Action.AccountsLoadingChanged(false))
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.OnAccountClicked -> publish(Label.OnAccountSelected(intent.id))
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.AccountLoaded -> dispatch(Msg.AccountLoaded(action.accounts))
                is Action.AccountsLoadingChanged -> dispatch(Msg.LoadingStatusChanged(action.isLoading))
                is Action.AccountsLoadingError -> {}
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.AccountLoaded -> copy(accounts = msg.accounts)
            is Msg.LoadingStatusChanged -> copy(isLoading = msg.isLoading)
        }
    }
}
