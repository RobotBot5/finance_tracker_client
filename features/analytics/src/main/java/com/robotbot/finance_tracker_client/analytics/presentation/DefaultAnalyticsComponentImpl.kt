package com.robotbot.finance_tracker_client.analytics.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultAnalyticsComponentImpl @AssistedInject constructor(
    private val storeFactory: AnalyticsStoreFactory,
    @Assisted componentContext: ComponentContext,
) : AnalyticsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                // Handle labels if needed
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AnalyticsStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory : AnalyticsComponent.Factory {
        override fun invoke(
            @Assisted componentContext: ComponentContext
        ): DefaultAnalyticsComponentImpl
    }
}