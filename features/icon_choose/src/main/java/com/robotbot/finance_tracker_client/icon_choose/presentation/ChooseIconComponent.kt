package com.robotbot.finance_tracker_client.icon_choose.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface ChooseIconComponent {

    val model: StateFlow<ChooseIconStore.State>

    fun onIconClicked(iconId: Long)

    fun interface Factory {
        operator fun invoke(
            yetSelectedIconId: Long?,
            onIconSelected: (Long) -> Unit,
            componentContext: ComponentContext
        ): ChooseIconComponent
    }
}
