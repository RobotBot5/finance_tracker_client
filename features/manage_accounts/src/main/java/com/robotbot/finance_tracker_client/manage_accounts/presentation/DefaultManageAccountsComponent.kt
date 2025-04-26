package com.robotbot.finance_tracker_client.manage_accounts.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsComponent.Events
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsStore.Intent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class DefaultManageAccountsComponent @AssistedInject constructor(
    private val storeFactory: ManageAccountsStoreFactory,
    @Assisted("editableAccountEntityId") private val editableAccountEntityId: Long?,
    @Assisted("onWorkFinished") private val onWorkFinished: () -> Unit,
    @Assisted("onChangeCurrency") private val onChangeCurrency: (String) -> Unit,
    @Assisted("onChangeIcon") private val onChangeIcon: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : ManageAccountsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(editableAccountEntityId) }
    private val scope = componentContext.componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    ManageAccountsStore.Label.WorkFinished -> {
                        onWorkFinished()
                    }
                    is ManageAccountsStore.Label.ErrorMsg -> {
                        _events.emit(Events.CreateAccountError(it.errorMsg))
                    }
                    is ManageAccountsStore.Label.ChooseCurrency -> onChangeCurrency(it.selectedCurrencyCode)
                    is ManageAccountsStore.Label.ChooseIcon -> onChangeIcon(it.yetSelectedIconId)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ManageAccountsStore.State> = store.stateFlow

    private val _events: MutableSharedFlow<Events> = MutableSharedFlow()
    override val events: SharedFlow<Events> = _events.asSharedFlow()

    override fun onChangeAccountTitle(title: String) {
        store.accept(Intent.ChangeAccountTitle(title))
    }

    override fun onChangeBalance(balance: String) {
        store.accept(Intent.ChangeBalance(balance))
    }

    override fun onClickCreateAccount() {
        store.accept(Intent.ClickSave)
    }

    override fun onSelectedCurrencyChanged(currencyCode: String) {
        store.accept(Intent.ChangeSelectedCurrency(currencyCode))
    }

    override fun onChangeCurrency() {
        store.accept(Intent.ChooseCurrencyClicked)
    }

    override fun onSelectedIconChanged(iconId: Long) {
        store.accept(Intent.ChangeSelectedIcon(iconId))
    }

    override fun onIconClicked() {
        store.accept(Intent.IconClicked)
    }

    override fun onDeleteClicked() {
        store.accept(Intent.ClickDelete)
    }

    @AssistedFactory
    interface Factory : ManageAccountsComponent.Factory {
        override fun invoke(
            @Assisted("editableAccountEntityId") editableAccountEntityId: Long?,
            @Assisted("onWorkFinished") onWorkFinished: () -> Unit,
            @Assisted("onChangeCurrency") onChangeCurrency: (String) -> Unit,
            @Assisted("onChangeIcon") onChangeIcon: (Long) -> Unit,
            componentContext: ComponentContext
        ): DefaultManageAccountsComponent
    }
}