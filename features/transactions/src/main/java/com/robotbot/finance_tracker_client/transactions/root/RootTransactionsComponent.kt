package com.robotbot.finance_tracker_client.transactions.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsComponent

interface RootTransactionsComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onTabClicked(tab: TransactionsNavTab)

    sealed interface Child {

        data class ExpenseTransactions(val component: TransactionsComponent) : Child

        data class IncomeTransactions(val component: TransactionsComponent) : Child
    }

    fun interface Factory {
        operator fun invoke(
            onCreateTransactionNavigate: (Long?) -> Unit,
            componentContext: ComponentContext
        ): RootTransactionsComponent
    }
}
