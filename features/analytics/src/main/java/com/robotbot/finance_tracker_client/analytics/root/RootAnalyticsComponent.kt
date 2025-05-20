package com.robotbot.finance_tracker_client.analytics.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsComponent

interface RootAnalyticsComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onTabClicked(tab: AnalyticsNavTab)

    sealed interface Child {

        data class ExpenseAnalytics(val component: AnalyticsComponent) : Child

        data class IncomeAnalytics(val component: AnalyticsComponent) : Child
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): RootAnalyticsComponent
    }
}
