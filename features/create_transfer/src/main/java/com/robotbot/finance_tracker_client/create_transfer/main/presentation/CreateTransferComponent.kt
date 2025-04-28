package com.robotbot.finance_tracker_client.create_transfer.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.robotbot.finance_tracker_client.create_transfer.ChangingAccountQualifier
import kotlinx.coroutines.flow.StateFlow

interface CreateTransferComponent {

    val model: StateFlow<CreateTransferStore.State>

    fun onAmountFromChanged(amountFromString: String)

    fun onAmountToChanged(amountToString: String?)

    fun onAccountFromIdChanged(accountFromId: Long)

    fun onAccountToIdChanged(accountToId: Long)

    fun onAccountFromClicked()

    fun onAccountToClicked()

    fun onCreateTransferClicked()

    fun interface Factory {
        operator fun invoke(
            onChangeAccountId: (ChangingAccountQualifier, yetSelectedAccountId: Long?) -> Unit,
            onTransferCreated: () -> Unit,
            componentContext: ComponentContext
        ): CreateTransferComponent
    }
}