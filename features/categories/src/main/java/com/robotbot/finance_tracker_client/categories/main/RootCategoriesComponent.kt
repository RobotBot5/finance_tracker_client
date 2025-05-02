package com.robotbot.finance_tracker_client.categories.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesComponent

interface RootCategoriesComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onTabClicked(tab: CategoriesNavTab)

    sealed interface Child {

        data class ExpenseCategories(val component: CategoriesComponent) : Child

        data class IncomeCategories(val component: CategoriesComponent) : Child
    }

    fun interface Factory {
        operator fun invoke(
            onCreateCategoryNavigate: () -> Unit,
            onEditCategoryNavigate: (Long) -> Unit,
            componentContext: ComponentContext
        ): RootCategoriesComponent
    }
}
