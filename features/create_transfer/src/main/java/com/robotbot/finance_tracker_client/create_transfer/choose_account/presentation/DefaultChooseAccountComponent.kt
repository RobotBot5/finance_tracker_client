package com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountStore.Intent
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultChooseAccountComponent @AssistedInject constructor(
    private val storeFactory: ChooseAccountStoreFactory,
    @Assisted("yetSelectedAccountId") private val yetSelectedAccountId: Long?,
    @Assisted("onAccountSelected") private val onAccountSelected: (id: Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : ChooseAccountComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(yetSelectedAccountId) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is ChooseAccountStore.Label.OnAccountSelected -> onAccountSelected(it.id)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ChooseAccountStore.State> = store.stateFlow

    override fun onAccountClicked(id: Long) {
        store.accept(Intent.OnAccountClicked(id))
    }

    @AssistedFactory
    interface Factory : ChooseAccountComponent.Factory {
        override fun invoke(
            @Assisted("yetSelectedAccountId") yetSelectedAccountId: Long?,
            @Assisted("onAccountSelected") onAccountSelected: (id: Long) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultChooseAccountComponent
    }
}