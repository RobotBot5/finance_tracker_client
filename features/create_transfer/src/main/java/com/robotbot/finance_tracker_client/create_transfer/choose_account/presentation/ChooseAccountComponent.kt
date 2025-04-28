package com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface ChooseAccountComponent {

    val model: StateFlow<ChooseAccountStore.State>

    fun onAccountClicked(id: Long)

    fun interface Factory {
        operator fun invoke(
            yetSelectedAccountId: Long?,
            onAccountSelected: (id: Long) -> Unit,
            componentContext: ComponentContext
        ): ChooseAccountComponent
    }
}