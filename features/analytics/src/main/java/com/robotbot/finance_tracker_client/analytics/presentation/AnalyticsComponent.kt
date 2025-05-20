package com.robotbot.finance_tracker_client.analytics.presentation

import com.arkivanov.decompose.ComponentContext
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import kotlinx.coroutines.flow.StateFlow

interface AnalyticsComponent {

    val model: StateFlow<AnalyticsStore.State>

    fun interface Factory {
        operator fun invoke(
            analyticsType: CategoryType,
            componentContext: ComponentContext
        ): AnalyticsComponent
    }
}
