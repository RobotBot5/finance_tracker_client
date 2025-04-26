package com.robotbot.finance_tracker_client.categories.presentation

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

internal class DefaultCategoriesComponent @AssistedInject constructor(
    private val storeFactory: CategoriesStoreFactory,
    @Assisted componentContext: ComponentContext
) : CategoriesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
//                when (it) {
//
//                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CategoriesStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory : CategoriesComponent.Factory {
        override fun invoke(
            @Assisted componentContext: ComponentContext
        ): DefaultCategoriesComponent
    }
}