package com.robotbot.finance_tracker_client.bank_accounts.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.common.AuthException
import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.entities.TotalBalanceEntity
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.Intent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.Label
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.State
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.State.AccountsState
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.State.TotalBalanceState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AccountsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ReloadAccounts : Intent

        data object CreateAccountClicked : Intent

        data class OnAccountClicked(val accountId: Long) : Intent

        data object OnCreateTransferClicked : Intent
    }

    data class State(
        val accountsState: AccountsState,
        val totalBalanceState: TotalBalanceState
    ) {

        val isLoading: Boolean
            get() = accountsState is AccountsState.Loading
                    || totalBalanceState is TotalBalanceState.Loading

        sealed interface AccountsState {

            data object Initial : AccountsState
            data object Loading : AccountsState
            data object Error : AccountsState
            data class Content(
                val accounts: List<AccountEntity>
            ) : AccountsState
        }

        sealed interface TotalBalanceState {
            data object Initial : TotalBalanceState
            data object Loading : TotalBalanceState
            data object Error : TotalBalanceState
            data class Content(
                val totalBalance: TotalBalanceEntity
            ) : TotalBalanceState
        }
    }

    sealed interface Label {

        data object AuthFailed : Label

        data object CreateAccountNavigate : Label

        data class EditAccount(val accountId: Long) : Label

        data object OnCreateTransferNavigate : Label
    }
}

internal class AccountsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val accountsRepository: BankAccountsRepository
) {

    private var loadAccountsJob: Job? = null
    private var totalBalanceLoadingJob: Job? = null

    fun create(): AccountsStore =
        object : AccountsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AccountsStore",
            initialState = State(
                accountsState = AccountsState.Initial,
                totalBalanceState = TotalBalanceState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object AccountsLoading : Action

        data object AccountsError : Action

        data class AccountsContent(val accounts: List<AccountEntity>) : Action

        data object AuthFailed : Action

        data class TotalBalanceStateChanged(val totalBalanceState: TotalBalanceState) : Action
    }

    private sealed interface Msg {

        data object AccountsLoading : Msg

        data object AccountsError : Msg

        data class AccountsContent(val accounts: List<AccountEntity>) : Msg

        data class TotalBalanceStateChanged(val totalBalanceState: TotalBalanceState) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.AccountsLoading)
            loadAccountsJob = scope.launch {
                try {
                    val accounts = accountsRepository.getAccounts()
                    dispatch(Action.AccountsContent(accounts))
                }
                catch (e: Exception) {
                    dispatch(Action.AccountsError)
                }
            }
            dispatch(Action.TotalBalanceStateChanged(TotalBalanceState.Loading))
            totalBalanceLoadingJob = scope.launch {
                try {
                    val totalBalance = accountsRepository.getTotalBalance()
                    dispatch(Action.TotalBalanceStateChanged(TotalBalanceState.Content(totalBalance)))
                } catch (e: Exception) {
                    dispatch(Action.TotalBalanceStateChanged(TotalBalanceState.Error))
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
                is Action.TotalBalanceStateChanged -> dispatch(Msg.TotalBalanceStateChanged(action.totalBalanceState))
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
                    totalBalanceLoadingJob?.cancel()
                    dispatch(Msg.TotalBalanceStateChanged(TotalBalanceState.Loading))
                    totalBalanceLoadingJob = scope.launch {
                        try {
                            val totalBalance = accountsRepository.getTotalBalance()
                            dispatch(
                                Msg.TotalBalanceStateChanged(
                                    TotalBalanceState.Content(
                                        totalBalance
                                    )
                                )
                            )
                        } catch (e: AuthException) {
                            publish(Label.AuthFailed)
                        } catch (e: Exception) {
                            dispatch(Msg.TotalBalanceStateChanged(TotalBalanceState.Error))
                        }
                    }
                }
                Intent.CreateAccountClicked -> {
                    publish(Label.CreateAccountNavigate)
                }
                is Intent.OnAccountClicked -> {
                    publish(Label.EditAccount(intent.accountId))
                }
                Intent.OnCreateTransferClicked -> publish(Label.OnCreateTransferNavigate)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.AccountsContent -> copy(accountsState = AccountsState.Content(msg.accounts))
            Msg.AccountsError -> copy(accountsState = AccountsState.Error)
            Msg.AccountsLoading -> copy(accountsState = AccountsState.Loading)
            is Msg.TotalBalanceStateChanged -> copy(totalBalanceState = msg.totalBalanceState)
        }
    }
}
