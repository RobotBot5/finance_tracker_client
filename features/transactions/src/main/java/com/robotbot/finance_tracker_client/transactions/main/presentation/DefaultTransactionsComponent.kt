package com.robotbot.finance_tracker_client.transactions.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore.Intent
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore.Label
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultTransactionsComponent @AssistedInject constructor(
    private val transactionsStoreFactory: TransactionsStoreFactory,
    @Assisted("transactionType") private val transactionType: CategoryType,
    @Assisted("onCreateTransactionNavigate") private val onCreateTransactionNavigate: (editableTransactionId: Long?) -> Unit,
    @Assisted componentContext: ComponentContext
) : TransactionsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { transactionsStoreFactory.create(transactionType) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is Label.OnManageTransactionNavigate -> onCreateTransactionNavigate(it.transactionId)
                }
            }
        }
    }

    override fun onCreateTransactionClicked() {
        store.accept(Intent.OnCreateTransactionClicked)
    }

    override fun onTransactionClicked(transactionId: Long) {
        store.accept(Intent.OnTransactionClicked(transactionId))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<TransactionsStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory : TransactionsComponent.Factory {
        override fun invoke(
            @Assisted("transactionType") transactionType: CategoryType,
            @Assisted("onCreateTransactionNavigate") onCreateTransactionNavigate: (editableTransactionId: Long?) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultTransactionsComponent
    }
}
