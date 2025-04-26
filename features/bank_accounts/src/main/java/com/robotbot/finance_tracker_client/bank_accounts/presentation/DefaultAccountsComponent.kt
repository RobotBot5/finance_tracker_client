package com.robotbot.finance_tracker_client.bank_accounts.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.Intent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.Label
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultAccountsComponent @AssistedInject constructor(
    private val storeFactory: AccountsStoreFactory,
    @Assisted("onAuthFailed") private val onAuthFailed: () -> Unit,
    @Assisted("onCreateAccount") private val onCreateAccount: () -> Unit,
    @Assisted("onEditAccount") private val onEditAccount: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : AccountsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    Label.AuthFailed -> onAuthFailed()
                    Label.CreateAccountNavigate -> onCreateAccount()
                    is Label.EditAccount -> onEditAccount(it.accountId)
                }
            }
        }
        doOnStart {
            store.accept(Intent.ReloadAccounts)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AccountsStore.State> = store.stateFlow

    override fun onCreateAccountClicked() {
        store.accept(Intent.CreateAccountClicked)
    }

    override fun onAccountClicked(accountId: Long) {
        store.accept(Intent.OnAccountClicked(accountId))
    }

    @AssistedFactory
    interface Factory : AccountsComponent.Factory {
        override fun invoke(
            @Assisted("onAuthFailed") onAuthFailed: () -> Unit,
            @Assisted("onCreateAccount") onCreateAccount: () -> Unit,
            @Assisted("onEditAccount") onEditAccount: (Long) -> Unit,
            componentContext: ComponentContext
        ): DefaultAccountsComponent
    }
}