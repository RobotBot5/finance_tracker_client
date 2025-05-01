package com.robotbot.finance_tracker_client.transactions.manage.presentation

import android.annotation.SuppressLint
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.categories.CategoriesRepository
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.transactions.TransactionsRepository
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsStore.Intent
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsStore.Label
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsStore.State
import com.robotbot.finance_tracker_client.transactions.manage.presentation.OpenReason.ADD
import com.robotbot.finance_tracker_client.transactions.manage.presentation.OpenReason.EDIT
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.CreateTransactionRequest
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.UpdateTransactionRequest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

interface ManageTransactionsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeAmount(val amountString: String) : Intent

        data class ChangeTime(val timeString: String) : Intent

        data class ChangeCategory(val categoryId: Long) : Intent

        data object CategoryClicked : Intent

        data class ChangeAccount(val accountId: Long) : Intent

        data object AccountClicked : Intent

        data object ClickSave : Intent

        data object ClickDelete : Intent
    }

    data class State(
        val amount: BigDecimal,
        val time: OffsetDateTime,
        val category: CategoryEntity?,
        val account: AccountEntity?,
        val openReason: OpenReason,
        val editableTransactionEntityId: Long?,
        val isLoading: Boolean
    )

    sealed interface Label {

        data object WorkFinished : Label

        data class ChooseCategory(val yetSelectedCategoryId: Long?) : Label

        data class ChooseAccount(val yetSelectedAccountId: Long?) : Label
    }
}

internal class ManageTransactionsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val transactionsRepository: TransactionsRepository,
    private val bankAccountsRepository: BankAccountsRepository,
    private val categoriesRepository: CategoriesRepository
) {

    fun create(editableTransactionEntityId: Long?): ManageTransactionsStore =
        @SuppressLint("NewApi")
        object : ManageTransactionsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ManageTransactionsStore",
            initialState = State(
                amount = BigDecimal.ZERO,
                time = OffsetDateTime.now(),
                category = null,
                account = null,
                openReason = if (editableTransactionEntityId == null) ADD else EDIT,
                editableTransactionEntityId = editableTransactionEntityId,
                isLoading = false
            ),
            bootstrapper = BootstrapperImpl(editableTransactionEntityId),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class EditableTransactionEntityLoaded(val transaction: TransactionEntity) : Action

        data class ChangeLoading(val isLoading: Boolean) : Action
    }

    private sealed interface Msg {

        data class ChangeAmount(val amount: BigDecimal) : Msg

        data class ChangeTime(val time: OffsetDateTime) : Msg

        data class ChangeCategory(val category: CategoryEntity) : Msg

        data class ChangeAccount(val account: AccountEntity) : Msg

        data class ChangeLoading(val isLoading: Boolean) : Msg

        data class EditableTransactionEntityLoaded(val transaction: TransactionEntity) : Msg
    }

    private inner class BootstrapperImpl(private val editableTransactionEntityId: Long?) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                try {
                    editableTransactionEntityId?.let {
                        dispatch(Action.ChangeLoading(true))
                        val transaction = transactionsRepository.getTransactionById(editableTransactionEntityId)
                        dispatch(Action.EditableTransactionEntityLoaded(transaction))
                        dispatch(Action.ChangeLoading(false))
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.ChangeLoading -> dispatch(Msg.ChangeLoading(action.isLoading))
                is Action.EditableTransactionEntityLoaded -> dispatch(Msg.EditableTransactionEntityLoaded(action.transaction))
            }
        }

        @SuppressLint("NewApi")
        override fun executeIntent(intent: Intent) {
            when(intent) {
                Intent.AccountClicked -> publish(Label.ChooseAccount(state().account?.id))
                Intent.CategoryClicked -> publish(Label.ChooseCategory(state().category?.id))
                is Intent.ChangeAccount -> {
                    scope.launch {
                        try {
                            val account = bankAccountsRepository.getAccountById(intent.accountId)
                            dispatch(Msg.ChangeAccount(account))
                        } catch (e: Exception) {}
                    }
                }
                is Intent.ChangeCategory -> {
                    scope.launch {
                        try {
                            val category = categoriesRepository.getCategoryById(intent.categoryId)
                            dispatch(Msg.ChangeCategory(category))
                        } catch (e: Exception) {}
                    }
                }
                is Intent.ChangeAmount -> dispatch(Msg.ChangeAmount(intent.amountString.toBigDecimal()))
                is Intent.ChangeTime -> dispatch(Msg.ChangeTime(OffsetDateTime.parse(intent.timeString)))
                Intent.ClickDelete -> {
                    scope.launch {
                        try {
                            transactionsRepository.deleteTransaction(state().editableTransactionEntityId!!)
                            publish(Label.WorkFinished)
                        } catch (e: Exception) {}
                    }
                }
                Intent.ClickSave -> {
                    val currentState = state()
                    if (currentState.editableTransactionEntityId == null) {
                        val createTransactionRequest = CreateTransactionRequest(
                            amount = currentState.amount,
                            categoryId = currentState.category!!.id,
                            accountId = currentState.account!!.id,
                            time = currentState.time
                        )
                        scope.launch {
                            transactionsRepository.addTransaction(createTransactionRequest)
                            publish(Label.WorkFinished)
                        }
                    } else {
                        val updateTransactionRequest = UpdateTransactionRequest(
                            amount = currentState.amount,
                            categoryId = currentState.category?.id,
                            accountId = currentState.account?.id,
                            time = currentState.time
                        )
                        scope.launch {
                            transactionsRepository.updateTransaction(currentState.editableTransactionEntityId, updateTransactionRequest)
                            publish(Label.WorkFinished)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeAccount -> copy(account = msg.account)
            is Msg.ChangeAmount -> copy(amount = msg.amount)
            is Msg.ChangeCategory -> copy(category = msg.category)
            is Msg.ChangeLoading -> copy(isLoading = msg.isLoading)
            is Msg.ChangeTime -> copy(time = msg.time)
            is Msg.EditableTransactionEntityLoaded -> copy(
                amount = msg.transaction.amount,
                time = msg.transaction.time,
                category = msg.transaction.category,
                account = msg.transaction.account
            )
        }
    }
}
