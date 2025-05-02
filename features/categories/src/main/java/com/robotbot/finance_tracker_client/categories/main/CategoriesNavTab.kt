package com.robotbot.finance_tracker_client.categories.main

sealed class CategoriesNavTab(val title: String) {
    data object Expenses : CategoriesNavTab("expenses")
    data object Income : CategoriesNavTab("income")

    companion object {
        val tabs = listOf(Expenses, Income)
    }
}
