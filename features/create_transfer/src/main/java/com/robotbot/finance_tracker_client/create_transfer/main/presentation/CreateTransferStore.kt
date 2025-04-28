package com.robotbot.finance_tracker_client.create_transfer.main.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.TransferCreateRequest
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferStore.Intent
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferStore.Label
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferStore.State
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

interface CreateTransferStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangedAmountFrom(val amountFromString: String) : Intent

        data class ChangedAmountTo(val amountToString: String?) : Intent

        data class ChangedAccountFromId(val accountFromId: Long) : Intent

        data class ChangedAccountToId(val accountToId: Long) : Intent

        data object AccountFromClicked : Intent

        data object AccountToClicked : Intent

        data object CreateTransferClicked : Intent
    }

    data class State(
        val amountFrom: BigDecimal,
        val amountTo: BigDecimal?,
        val accountFrom: AccountEntity?,
        val accountTo: AccountEntity?
    )

    sealed interface Label {

        data class ChangeAccountFromId(val yetSelectedAccountFromId: Long?) : Label

        data class ChangeAccountToId(val yetSelectedAccountToId: Long?) : Label

        data object WorkFinished : Label
    }
}

internal class CreateTransferStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val bankAccountsRepository: BankAccountsRepository
) {

    fun create(): CreateTransferStore =
        object : CreateTransferStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CreateTransferStore",
            initialState = State(
                amountFrom = BigDecimal.ZERO,
                amountTo = null,
                accountFrom = null,
                accountTo = null
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class ChangedAmountFrom(val amountFrom: BigDecimal) : Msg

        data class ChangedAmountTo(val amountTo: BigDecimal?) : Msg

        data class ChangedAccountFrom(val accountFrom: AccountEntity) : Msg

        data class ChangedAccountTo(val accountTo: AccountEntity) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangedAccountFromId -> {
                    scope.launch {
                        val account = bankAccountsRepository.getAccountById(intent.accountFromId)
                        dispatch(Msg.ChangedAccountFrom(account))
                    }
                }
                is Intent.ChangedAccountToId -> {
                    scope.launch {
                        val account = bankAccountsRepository.getAccountById(intent.accountToId)
                        dispatch(Msg.ChangedAccountTo(account))
                    }
                }
                is Intent.ChangedAmountFrom -> dispatch(Msg.ChangedAmountFrom(intent.amountFromString.toBigDecimal()))
                is Intent.ChangedAmountTo -> dispatch(Msg.ChangedAmountTo(intent.amountToString?.toBigDecimal()))
                Intent.AccountFromClicked -> publish(Label.ChangeAccountFromId(state().accountFrom?.id))
                Intent.AccountToClicked -> publish(Label.ChangeAccountToId(state().accountTo?.id))
                Intent.CreateTransferClicked -> {
                    val transferCreateRequest = TransferCreateRequest(
                        amountFrom = state().amountFrom,
                        amountTo = state().amountTo,
                        accountFromId = state().accountFrom!!.id,
                        accountToId = state().accountTo!!.id
                    )
                    scope.launch {
                        try {
                            bankAccountsRepository.transfer(transferCreateRequest)
                            publish(Label.WorkFinished)
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangedAccountFrom -> copy(accountFrom = msg.accountFrom)
            is Msg.ChangedAccountTo -> copy(accountTo = msg.accountTo)
            is Msg.ChangedAmountFrom -> copy(amountFrom = msg.amountFrom)
            is Msg.ChangedAmountTo -> copy(amountTo = msg.amountTo)
        }
    }
}