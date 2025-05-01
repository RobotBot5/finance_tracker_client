package com.robotbot.finance_tracker_client.transactions.main.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface TransactionsComponent {

    val model: StateFlow<TransactionsStore.State>

    fun onCreateTransactionClicked()

    fun onTransactionClicked(transactionId: Long)

    interface Factory {
        operator fun invoke(
            onCreateTransactionNavigate: (editableTransactionId: Long?) -> Unit,
            componentContext: ComponentContext
        ): TransactionsComponent
    }
}