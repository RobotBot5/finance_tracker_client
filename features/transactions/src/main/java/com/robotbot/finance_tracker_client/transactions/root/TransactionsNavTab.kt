package com.robotbot.finance_tracker_client.transactions.root

sealed class TransactionsNavTab(val title: String) {
    data object Expenses : TransactionsNavTab("expenses")
    data object Income : TransactionsNavTab("income")

    companion object {
        val tabs = listOf(Expenses, Income)
    }
}
