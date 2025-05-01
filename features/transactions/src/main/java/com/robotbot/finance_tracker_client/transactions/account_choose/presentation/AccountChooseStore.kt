//package com.robotbot.finance_tracker_client.transactions.account_choose.presentation
//
//import com.arkivanov.mvikotlin.core.store.Reducer
//import com.arkivanov.mvikotlin.core.store.Store
//import com.arkivanov.mvikotlin.core.store.StoreFactory
//import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
//import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
//import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
//import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
//import com.robotbot.finance_tracker_client.transactions.account_choose.presentation.AccountChooseStore.Intent
//import com.robotbot.finance_tracker_client.transactions.account_choose.presentation.AccountChooseStore.Label
//import com.robotbot.finance_tracker_client.transactions.account_choose.presentation.AccountChooseStore.State
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//interface AccountChooseStore : Store<Intent, State, Label> {
//
//    sealed interface Intent {
//
//        data class SelectAccount(val id: Long) : Intent
//    }
//
//    data class State(
//        val isLoading: Boolean,
//        val accountsList: List<AccountEntity>,
//        val yetSelectedAccountId: Long?
//    )
//
//    sealed interface Label {
//
//        data class AccountSelected(val accountId: Long) : Label
//    }
//}
//
//internal class AccountChooseStoreFactory @Inject constructor(
//    private val storeFactory: StoreFactory,
//    private val bankAccountsRepository: BankAccountsRepository
//) {
//
//    fun create(yetSelectedAccountId: Long?): AccountChooseStore =
//        object : AccountChooseStore, Store<Intent, State, Label> by storeFactory.create(
//            name = "AccountChooseStore",
//            initialState = State(
//                isLoading = true,
//                accountsList = listOf(),
//                yetSelectedAccountId = yetSelectedAccountId
//            ),
//            bootstrapper = BootstrapperImpl(),
//            executorFactory = ::ExecutorImpl,
//            reducer = ReducerImpl
//        ) {}
//
//    private sealed interface Action {
//
//        data class AccountsLoading(val isLoading: Boolean) : Action
//        data class AccountsLoaded(val accounts: List<AccountEntity>) : Action
//        data class AccountsError(val errorMsg: String) : Action
//    }
//
//    private sealed interface Msg {
//
//        data class AccountsLoading(val isLoading: Boolean) : Msg
//        data class AccountsLoaded(val accounts: List<AccountEntity>) : Msg
//        data class AccountsError(val errorMsg: String) : Msg
//    }
//
//    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
//        override fun invoke() {
//            scope.launch {
//                dispatch(Action.AccountsLoading(true))
//                try {
//                    val accounts = bankAccountsRepository.getAccounts()
//                    dispatch(Action.AccountsLoaded(accounts))
//                } catch (e: Exception) {
//                    dispatch(Action.AccountsError(e.message ?: "Unknown error"))
//                } finally {
//                    dispatch(Action.AccountsLoading(false))
//                }
//            }
//        }
//    }
//
//    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
//        override fun executeIntent(intent: Intent) {
//            when (intent) {
//                is Intent.SelectAccount -> publish(Label.AccountSelected(intent.id))
//            }
//        }
//
//        override fun executeAction(action: Action) {
//            when (action) {
//                is Action.AccountsError -> dispatch(Msg.AccountsError(action.errorMsg))
//                is Action.AccountsLoaded -> dispatch(Msg.AccountsLoaded(action.accounts))
//                is Action.AccountsLoading -> dispatch(Msg.AccountsLoading(action.isLoading))
//            }
//        }
//    }
//
//    private object ReducerImpl : Reducer<State, Msg> {
//        override fun State.reduce(msg: Msg): State = when (msg) {
//            is Msg.AccountsError -> copy()
//            is Msg.AccountsLoaded -> copy(accountsList = msg.accounts)
//            is Msg.AccountsLoading -> copy(isLoading = msg.isLoading)
//        }
//    }
//}
