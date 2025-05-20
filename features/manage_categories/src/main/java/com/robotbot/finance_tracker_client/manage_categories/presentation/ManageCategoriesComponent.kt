package com.robotbot.finance_tracker_client.manage_categories.presentation

import com.arkivanov.decompose.ComponentContext
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ManageCategoriesComponent {

    val model: StateFlow<ManageCategoriesStore.State>

    val events: SharedFlow<Events>

    fun onChangeCategoryName(name: String)

    fun onChangeCategoryType(type: CategoryType)

    fun onClickCreateCategory()

    fun onSelectedIconChanged(iconId: Long)

    fun onIconClicked()

    fun onDeleteClicked()

    fun onReloadClicked()

    sealed interface Events {

        data class ErrorToast(val msg: String) : Events
    }

    fun interface Factory {
        operator fun invoke(
            editableCategoryEntityId: Long?,
            onWorkFinished: () -> Unit,
            onChangeIcon: (Long?) -> Unit,
            componentContext: ComponentContext
        ): ManageCategoriesComponent
    }
}
