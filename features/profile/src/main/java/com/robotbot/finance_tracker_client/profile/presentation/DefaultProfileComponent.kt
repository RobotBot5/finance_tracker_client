package com.robotbot.finance_tracker_client.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import com.robotbot.finance_tracker_client.profile.presentation.ProfileStore.Intent
import com.robotbot.finance_tracker_client.profile.presentation.ProfileStore.Label
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultProfileComponent @AssistedInject constructor(
    private val storeFactory: ProfileStoreFactory,
    @Assisted("onLogoutNavigate") private val onLogoutNavigate: () -> Unit,
    @Assisted("onChangeTargetCurrencyNavigate") private val onChangeTargetCurrencyNavigate: (yetSelectedCurrencyCode: String) -> Unit,
    @Assisted componentContext: ComponentContext
) : ProfileComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    Label.LogoutNavigate -> onLogoutNavigate()
                    is Label.TargetCurrencyChangeNavigate -> onChangeTargetCurrencyNavigate(it.yetSelectedTargetCurrencyCode)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ProfileStore.State> = store.stateFlow

    override fun onLogoutClicked() {
        store.accept(Intent.LogoutClicked)
    }

    override fun onTargetCurrencyClicked() {
        store.accept(Intent.TargetCurrencyClicked)
    }

    override fun onTargetCurrencyChanged(newTargetCurrencyCode: String) {
        store.accept(Intent.TargetCurrencyChanged(newTargetCurrencyCode))
    }

    @AssistedFactory
    interface Factory : ProfileComponent.Factory {
        override fun invoke(
            @Assisted("onLogoutNavigate") onLogoutNavigate: () -> Unit,
            @Assisted("onChangeTargetCurrencyNavigate") onChangeTargetCurrencyNavigate: (yetSelectedCurrencyCode: String) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultProfileComponent
    }
}
