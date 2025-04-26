package com.robotbot.finance_tracker_client.manage_accounts.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountCreateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountUpdateRequest
import com.robotbot.finance_tracker_client.get_info.GetInfoRepository
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsStore.Intent
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsStore.Label
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsStore.State
import com.robotbot.finance_tracker_client.manage_accounts.presentation.OpenReason.CREATE
import com.robotbot.finance_tracker_client.manage_accounts.presentation.OpenReason.EDIT
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

interface ManageAccountsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeAccountTitle(val title: String) : Intent

        data class ChangeBalance(val balanceString: String) : Intent

        data object ChooseCurrencyClicked : Intent

        data class ChangeSelectedCurrency(val currencyCode: String) : Intent

        data class ChangeSelectedIcon(val iconId: Long) : Intent

        data object IconClicked : Intent

        data object ClickSave : Intent

        data object ClickDelete : Intent
    }

    data class State(
        val accountTitle: String,
        val balance: BigDecimal,
        val selectedCurrency: CurrencyEntity,
        val selectedIconEntity: IconEntity,
        val openReason: OpenReason,
        val editableAccountId: Long?,
        val isLoading: Boolean
    )

    sealed interface Label {

        data object WorkFinished : Label

        data class ErrorMsg(val errorMsg: String) : Label

        data class ChooseCurrency(val selectedCurrencyCode: String) : Label

        data class ChooseIcon(val yetSelectedIconId: Long) : Label
    }
}

internal class ManageAccountsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val bankAccountsRepository: BankAccountsRepository,
    private val getInfoRepository: GetInfoRepository
) {

    fun create(editableAccountId: Long?): ManageAccountsStore =
        object : ManageAccountsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ManageAccountsStore",
            initialState = State(
                accountTitle = "",
                balance = BigDecimal.ZERO,
                selectedCurrency = CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
                selectedIconEntity = IconEntity(id = 1, name = "dasd", path = "/icons/account_card_24dp.svg"),
                openReason = if (editableAccountId == null) CREATE else EDIT,
                editableAccountId = editableAccountId,
                isLoading = false
            ),
            bootstrapper = BootstrapperImpl(editableAccountId),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class EditableAccountLoaded(val account: AccountEntity) : Action
    }

    private sealed interface Msg {

        data class ChangeAccountTitle(val title: String) : Msg

        data class ChangeBalance(val balanceString: String) : Msg

        data class ChangeLoading(val isLoading: Boolean) : Msg

        data class ChangeSelectedCurrency(val currency: CurrencyEntity) : Msg

        data class ChangeSelectedIcon(val icon: IconEntity) : Msg

        data class EditableAccountLoaded(val account: AccountEntity) : Msg
    }

    private inner class BootstrapperImpl(private val editableAccountId: Long?) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            editableAccountId?.let {
                scope.launch {
                    val editableAccount = bankAccountsRepository.getAccountById(editableAccountId)
                    dispatch(Action.EditableAccountLoaded(editableAccount))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.EditableAccountLoaded -> dispatch(Msg.EditableAccountLoaded(action.account))
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeAccountTitle -> {
                    dispatch(Msg.ChangeAccountTitle(intent.title))
                }
                is Intent.ChangeBalance -> {
                    dispatch(Msg.ChangeBalance(intent.balanceString))
                }
                Intent.ClickSave -> {
                    val currentState = state()
                    if (currentState.openReason == CREATE) {
                        val accountCreateRequest = AccountCreateRequest(
                            name = currentState.accountTitle,
                            currencyCode = currentState.selectedCurrency.code,
                            iconId = currentState.selectedIconEntity.id,
                            balance = currentState.balance
                        )
                        scope.launch {
                            try {
                                dispatch(Msg.ChangeLoading(true))
                                bankAccountsRepository.addAccount(accountCreateRequest)
                                publish(Label.WorkFinished)
                            } catch (e: Exception) {
                                publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                            } finally {
                                dispatch(Msg.ChangeLoading(false))
                            }
                        }
                    } else if (currentState.openReason == EDIT) {
                        val accountUpdateRequest = AccountUpdateRequest(
                            name = currentState.accountTitle,
                            currencyCode = currentState.selectedCurrency.code,
                            iconId = currentState.selectedIconEntity.id,
                            balance = currentState.balance
                        )
                        scope.launch {
                            try {
                                dispatch(Msg.ChangeLoading(true))
                                currentState.editableAccountId?.let {
                                    bankAccountsRepository.updateAccount(it, accountUpdateRequest)
                                }
                                publish(Label.WorkFinished)
                            } catch (e: Exception) {
                                publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                            } finally {
                                dispatch(Msg.ChangeLoading(false))
                            }
                        }
                    }
                }
                is Intent.ChangeSelectedCurrency -> {
                    scope.launch {
                        val currencyEntity = getInfoRepository.getCurrencyByCode(intent.currencyCode)
                        dispatch(Msg.ChangeSelectedCurrency(currencyEntity))
                    }
                }
                Intent.ChooseCurrencyClicked -> publish(Label.ChooseCurrency(state().selectedCurrency.code))
                is Intent.ChangeSelectedIcon -> {
                    scope.launch {
                        val iconEntity = getInfoRepository.getIconById(intent.iconId)
                        dispatch(Msg.ChangeSelectedIcon(iconEntity))
                    }
                }
                Intent.IconClicked -> publish(Label.ChooseIcon(state().selectedIconEntity.id))
                Intent.ClickDelete -> {
                    scope.launch {
                        try {
                            dispatch(Msg.ChangeLoading(true))
                            state().editableAccountId?.let {
                                bankAccountsRepository.deleteAccount(it)
                            }
                            publish(Label.WorkFinished)
                        } catch (e: Exception) {
                            publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                        } finally {
                            dispatch(Msg.ChangeLoading(false))
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeAccountTitle -> copy(accountTitle = msg.title)
            is Msg.ChangeBalance -> copy(balance = msg.balanceString.toBigDecimal())
            is Msg.ChangeLoading -> copy(isLoading = msg.isLoading)
            is Msg.ChangeSelectedCurrency -> copy(selectedCurrency = msg.currency)
            is Msg.ChangeSelectedIcon -> copy(selectedIconEntity = msg.icon)
            is Msg.EditableAccountLoaded -> copy(
                accountTitle = msg.account.name,
                balance = msg.account.balance,
                selectedCurrency = msg.account.currency,
                selectedIconEntity = msg.account.icon
            )
        }
    }
}
