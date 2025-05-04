package com.robotbot.finance_tracker_client.transactions.manage.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface ManageTransactionsComponent {

    val model: StateFlow<ManageTransactionsStore.State>

    fun onChangeAmount(amountString: String)

    fun onChangeDate(date: LocalDate)

    fun onCategoryClicked()

    fun onAccountClicked()

    fun onSaveClicked()

    fun onDeleteClicked()

    fun onCategoryChanged(categoryId: Long)

    fun onAccountChanged(accountId: Long)

    interface Factory {
        operator fun invoke(
            editableTransactionId: Long?,
            onChooseAccount: (yetSelectedAccountId: Long?) -> Unit,
            onChooseCategory: (yetSelectedCategoryId: Long?) -> Unit,
            onWorkFinished: () -> Unit,
            componentContext: ComponentContext
        ): ManageTransactionsComponent
    }
}
