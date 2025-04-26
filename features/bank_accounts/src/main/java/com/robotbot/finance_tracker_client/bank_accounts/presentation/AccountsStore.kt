package com.robotbot.finance_tracker_client.bank_accounts.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.common.AuthException
import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.Intent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.Label
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.State
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.State.AccountsState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AccountsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ReloadAccounts : Intent

        data object CreateAccountClicked : Intent

        data class OnAccountClicked(val accountId: Long) : Intent
    }

    data class State(val accountsState: AccountsState) {

        sealed interface AccountsState {

            data object Initial : AccountsState
            data object Loading : AccountsState
            data object Error : AccountsState
            data class Content(
                val accounts: List<AccountEntity>
            ) : AccountsState
        }
    }

    sealed interface Label {

        data object AuthFailed : Label

        data object CreateAccountNavigate : Label

        data class EditAccount(val accountId: Long) : Label
    }
}

internal class AccountsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val accountsRepository: BankAccountsRepository
) {

    private var loadAccountsJob: Job? = null

    fun create(): AccountsStore =
        object : AccountsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AccountsStore",
            initialState = State(accountsState = AccountsState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object AccountsLoading : Action

        data object AccountsError : Action

        data class AccountsContent(val accounts: List<AccountEntity>) : Action

        data object AuthFailed : Action
    }

    private sealed interface Msg {

        data object AccountsLoading : Msg

        data object AccountsError : Msg

        data class AccountsContent(val accounts: List<AccountEntity>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.AccountsLoading)
            loadAccountsJob = scope.launch {
                try {
                    val accounts = accountsRepository.getAccounts()
                    dispatch(Action.AccountsContent(accounts))
                } catch (e: AuthException) {
                    dispatch(Action.AuthFailed)
                }
                catch (e: Exception) {
                    dispatch(Action.AccountsError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.AccountsContent -> dispatch(Msg.AccountsContent(action.accounts))
                Action.AccountsError -> dispatch(Msg.AccountsError)
                Action.AccountsLoading -> dispatch(Msg.AccountsLoading)
                Action.AuthFailed -> publish(Label.AuthFailed)
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ReloadAccounts -> {
                    loadAccountsJob?.cancel()
                    dispatch(Msg.AccountsLoading)
                    loadAccountsJob = scope.launch {
                        try {
                            val accounts = accountsRepository.getAccounts()
                            dispatch(Msg.AccountsContent(accounts))
                        } catch (e: AuthException) {
                            publish(Label.AuthFailed)
                        }
                        catch (e: Exception) {
                            dispatch(Msg.AccountsError)
                        }
                    }
                }
                Intent.CreateAccountClicked -> {
                    publish(Label.CreateAccountNavigate)
                }
                is Intent.OnAccountClicked -> {
                    publish(Label.EditAccount(intent.accountId))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.AccountsContent -> copy(accountsState = AccountsState.Content(msg.accounts))
            Msg.AccountsError -> copy(accountsState = AccountsState.Error)
            Msg.AccountsLoading -> copy(accountsState = AccountsState.Loading)
        }
    }
}
