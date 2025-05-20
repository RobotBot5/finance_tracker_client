package com.robotbot.finance_tracker_client.categories.presentation

import com.arkivanov.decompose.ComponentContext
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import kotlinx.coroutines.flow.StateFlow

interface CategoriesComponent {

    val model: StateFlow<CategoriesStore.State>

    fun onCreateCategoryClicked()

    fun onCategoryClicked(categoryId: Long)

    fun interface Factory {
        operator fun invoke(
            categoryType: CategoryType,
            onCreateCategory: () -> Unit,
            onEditCategory: (Long) -> Unit,
            componentContext: ComponentContext
        ): CategoriesComponent
    }
}
