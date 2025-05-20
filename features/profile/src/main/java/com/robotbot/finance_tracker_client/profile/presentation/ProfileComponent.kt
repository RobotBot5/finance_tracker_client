package com.robotbot.finance_tracker_client.profile.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {

    val model: StateFlow<ProfileStore.State>

    fun onLogoutClicked()

    fun onTargetCurrencyClicked()

    fun onTargetCurrencyChanged(newTargetCurrencyCode: String)

    interface Factory {
        operator fun invoke(
            onLogoutNavigate: () -> Unit,
            onChangeTargetCurrencyNavigate: (yetSelectedCurrencyCode: String) -> Unit,
            componentContext: ComponentContext
        ): ProfileComponent
    }
}
