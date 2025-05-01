package com.robotbot.finance_tracker_client.transactions.manage.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsStore.Intent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultManageTransactionsComponent @AssistedInject constructor(
    private val manageTransactionsStoreFactory: ManageTransactionsStoreFactory,
    @Assisted("editableTransactionId") private val editableTransactionId: Long?,
    @Assisted("onChooseAccount") private val onChooseAccount: (yetSelectedAccountId: Long?) -> Unit,
    @Assisted("onChooseCategory") private val onChooseCategory: (yetSelectedCategoryId: Long?) -> Unit,
    @Assisted("onWorkFinished") private val onWorkFinished: () -> Unit,
    @Assisted componentContext: ComponentContext
) : ManageTransactionsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { manageTransactionsStoreFactory.create(editableTransactionId) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is ManageTransactionsStore.Label.ChooseAccount -> onChooseAccount(it.yetSelectedAccountId)
                    is ManageTransactionsStore.Label.ChooseCategory -> onChooseCategory(it.yetSelectedCategoryId)
                    ManageTransactionsStore.Label.WorkFinished -> onWorkFinished()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ManageTransactionsStore.State> = store.stateFlow

    override fun onChangeAmount(amountString: String) {
        store.accept(Intent.ChangeAmount(amountString))
    }

    override fun onChangeTime(timeString: String) {
        store.accept(Intent.ChangeTime(timeString))
    }

    override fun onCategoryClicked() {
        store.accept(Intent.CategoryClicked)
    }

    override fun onAccountClicked() {
        store.accept(Intent.AccountClicked)
    }

    override fun onSaveClicked() {
        store.accept(Intent.ClickSave)
    }

    override fun onDeleteClicked() {
        store.accept(Intent.ClickDelete)
    }

    override fun onCategoryChanged(categoryId: Long) {
        store.accept(Intent.ChangeCategory(categoryId))
    }

    override fun onAccountChanged(accountId: Long) {
        store.accept(Intent.ChangeAccount(accountId))
    }

    @AssistedFactory
    interface Factory : ManageTransactionsComponent.Factory {
        override fun invoke(
            @Assisted("editableTransactionId") editableTransactionId: Long?,
            @Assisted("onChooseAccount") onChooseAccount: (yetSelectedAccountId: Long?) -> Unit,
            @Assisted("onChooseCategory") onChooseCategory: (yetSelectedCategoryId: Long?) -> Unit,
            @Assisted("onWorkFinished") onWorkFinished: () -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultManageTransactionsComponent
    }
}