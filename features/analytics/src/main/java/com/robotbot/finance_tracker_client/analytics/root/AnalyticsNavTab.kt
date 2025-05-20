package com.robotbot.finance_tracker_client.analytics.root

sealed class AnalyticsNavTab(val title: String) {
    data object Expenses : AnalyticsNavTab("expenses")
    data object Income : AnalyticsNavTab("income")

    companion object {
        val tabs = listOf(Expenses, Income)
    }
}
