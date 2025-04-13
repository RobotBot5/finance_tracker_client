package com.robotbot.finance_tracker_client.bank_accounts.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface AccountsComponent {

    val model: StateFlow<AccountsStore.State>

    fun interface Factory {
        operator fun invoke(
            onAuthFailed: () -> Unit,
            componentContext: ComponentContext
        ): AccountsComponent
    }
}