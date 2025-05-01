//package com.robotbot.finance_tracker_client.transactions.account_choose.presentation
//
//import com.arkivanov.decompose.ComponentContext
//import com.arkivanov.mvikotlin.core.instancekeeper.getStore
//import com.arkivanov.mvikotlin.extensions.coroutines.labels
//import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
//import com.robotbot.finance_tracker_client.dependencies.util.componentScope
//import com.robotbot.finance_tracker_client.transactions.account_choose.presentation.AccountChooseStore.Intent
//import com.robotbot.finance_tracker_client.transactions.account_choose.presentation.AccountChooseStore.Label
//import dagger.assisted.Assisted
//import dagger.assisted.AssistedFactory
//import dagger.assisted.AssistedInject
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//internal class DefaultAccountChooseComponent @AssistedInject constructor(
//    private val storeFactory: AccountChooseStoreFactory,
//    @Assisted("yetSelectedAccountId") private val yetSelectedAccountId: Long?,
//    @Assisted("onAccountSelected") private val onAccountSelected: (Long) -> Unit,
//    @Assisted componentContext: ComponentContext
//) : AccountChooseComponent, ComponentContext by componentContext {
//
//    private val store = instanceKeeper.getStore { storeFactory.create(yetSelectedAccountId) }
//    private val scope = componentScope()
//
//    init {
//        scope.launch {
//            store.labels.collect {
//                when (it) {
//                    is Label.AccountSelected -> {
//                        onAccountSelected(it.accountId)
//                    }
//                }
//            }
//        }
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    override val model: StateFlow<AccountChooseStore.State> = store.stateFlow
//
//    override fun onAccountClicked(accountId: Long) {
//        store.accept(Intent.SelectAccount(accountId))
//    }
//
//    @AssistedFactory
//    interface Factory : AccountChooseComponent.Factory {
//        override fun invoke(
//            @Assisted("yetSelectedAccountId") yetSelectedAccountId: Long?,
//            @Assisted("onAccountSelected") onAccountSelected: (Long) -> Unit,
//            @Assisted componentContext: ComponentContext
//        ): DefaultAccountChooseComponent
//    }
//}