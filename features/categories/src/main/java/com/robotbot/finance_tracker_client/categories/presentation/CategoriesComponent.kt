package com.robotbot.finance_tracker_client.categories.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface CategoriesComponent {

    val model: StateFlow<CategoriesStore.State>

    fun onCreateCategoryClicked()

    fun onCategoryClicked(accountId: Long)

    fun interface Factory {
        operator fun invoke(
            onCreateAccount: () -> Unit,
            onEditAccount: (Long) -> Unit,
            componentContext: ComponentContext
        ): CategoriesComponent
    }
}