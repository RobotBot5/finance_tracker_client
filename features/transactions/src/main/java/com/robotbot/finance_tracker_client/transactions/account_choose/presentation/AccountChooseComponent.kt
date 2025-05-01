//package com.robotbot.finance_tracker_client.transactions.account_choose.presentation
//
//import com.arkivanov.decompose.ComponentContext
//import kotlinx.coroutines.flow.StateFlow
//
//interface AccountChooseComponent {
//
//    val model: StateFlow<AccountChooseStore.State>
//
//    fun onAccountClicked(accountId: Long)
//
//    fun interface Factory {
//        operator fun invoke(
//            yetSelectedAccountId: Long?,
//            onAccountSelected: (Long) -> Unit,
//            componentContext: ComponentContext
//        ): AccountChooseComponent
//    }
//}