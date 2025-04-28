package com.robotbot.finance_tracker_client.create_transfer.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.create_transfer.ChangingAccountQualifier
import com.robotbot.finance_tracker_client.create_transfer.ChangingAccountQualifier.FROM
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferStore.Intent
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultCreateTransferComponent @AssistedInject constructor(
    private val storeFactory: CreateTransferStoreFactory,
    @Assisted("onChangeAccountId") private val onChangeAccountId: (ChangingAccountQualifier, yetSelectedAccountId: Long?) -> Unit,
    @Assisted("onTransferCreated") private val onTransferCreated: () -> Unit,
    @Assisted componentContext: ComponentContext
) : CreateTransferComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is CreateTransferStore.Label.ChangeAccountFromId -> onChangeAccountId(FROM, it.yetSelectedAccountFromId)
                    is CreateTransferStore.Label.ChangeAccountToId -> onChangeAccountId(ChangingAccountQualifier.TO, it.yetSelectedAccountToId)
                    CreateTransferStore.Label.WorkFinished -> onTransferCreated()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CreateTransferStore.State> = store.stateFlow

    override fun onAmountFromChanged(amountFromString: String) {
        store.accept(Intent.ChangedAmountFrom(amountFromString))
    }

    override fun onAmountToChanged(amountToString: String?) {
        store.accept(Intent.ChangedAmountTo(amountToString))
    }

    override fun onAccountFromIdChanged(accountFromId: Long) {
        store.accept(Intent.ChangedAccountFromId(accountFromId))
    }

    override fun onAccountToIdChanged(accountToId: Long) {
        store.accept(Intent.ChangedAccountToId(accountToId))
    }

    override fun onAccountFromClicked() {
        store.accept(Intent.AccountFromClicked)
    }

    override fun onAccountToClicked() {
        store.accept(Intent.AccountToClicked)
    }

    override fun onCreateTransferClicked() {
        store.accept(Intent.CreateTransferClicked)
    }

    @AssistedFactory
    interface Factory : CreateTransferComponent.Factory {
        override fun invoke(
            @Assisted("onChangeAccountId") onChangeAccountId: (ChangingAccountQualifier, yetSelectedAccountId: Long?) -> Unit,
            @Assisted("onTransferCreated") onTransferCreated: () -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultCreateTransferComponent
    }
}