package com.robotbot.finance_tracker_client.manage_accounts.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ManageAccountsComponent {

    val model: StateFlow<ManageAccountsStore.State>

    val events: SharedFlow<Events>

    fun onChangeAccountTitle(title: String)

    fun onChangeBalance(balance: String)

    fun onClickCreateAccount()

    fun onSelectedCurrencyChanged(currencyCode: String)

    fun onChangeCurrency()

    fun onSelectedIconChanged(iconId: Long)

    fun onIconClicked()

    fun onDeleteClicked()

    sealed interface Events {

        data class CreateAccountError(val msg: String) : Events
    }

    fun interface Factory {
        operator fun invoke(
            editableAccountEntityId: Long?,
            onWorkFinished: () -> Unit,
            onChangeCurrency: (String?) -> Unit,
            onChangeIcon: (Long?) -> Unit,
            componentContext: ComponentContext
        ): ManageAccountsComponent
    }
}
