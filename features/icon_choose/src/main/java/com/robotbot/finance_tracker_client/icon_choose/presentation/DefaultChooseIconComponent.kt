package com.robotbot.finance_tracker_client.icon_choose.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconStore.Label
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultChooseIconComponent @AssistedInject constructor(
    private val storeFactory: ChooseIconsStoreFactory,
    @Assisted("yetSelectedIconId") private val yetSelectedIconId: Long,
    @Assisted("onIconSelected") private val onIconSelected: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : ChooseIconComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(yetSelectedIconId) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is Label.IconSelected -> {
                        onIconSelected(it.iconId)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ChooseIconStore.State> = store.stateFlow

    override fun onIconClicked(iconId: Long) {
        store.accept(ChooseIconStore.Intent.SelectIcon(iconId))
    }

    @AssistedFactory
    interface Factory : ChooseIconComponent.Factory {
        override fun invoke(
            @Assisted("yetSelectedIconId") yetSelectedIconId: Long,
            @Assisted("onIconSelected") onIconSelected: (Long) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultChooseIconComponent
    }
}