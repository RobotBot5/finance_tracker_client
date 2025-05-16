package com.robotbot.finance_tracker_client.analytics.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface AnalyticsComponent {

    val model: StateFlow<AnalyticsStore.State>

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): AnalyticsComponent
    }
}