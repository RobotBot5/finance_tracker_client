package com.robotbot.finance_tracker_client.transactions.category_choose.presentation

import com.arkivanov.decompose.ComponentContext
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import kotlinx.coroutines.flow.StateFlow

interface CategoryChooseComponent {

    val model: StateFlow<CategoryChooseStore.State>

    fun onCategoryClicked(categoryId: Long)

    fun onChangeCategoryType(categoryType: CategoryType)

    fun interface Factory {
        operator fun invoke(
            yetSelectedCategoryId: Long?,
            onCategorySelected: (Long) -> Unit,
            componentContext: ComponentContext
        ): CategoryChooseComponent
    }
}
