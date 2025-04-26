package com.robotbot.finance_tracker_client.manage_categories.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesComponent.Events
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesStore.Intent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class DefaultManageCategoriesComponent @AssistedInject constructor(
    private val storeFactory: ManageCategoriesStoreFactory,
    @Assisted("editableCategoryEntityId") private val editableCategoryEntityId: Long?,
    @Assisted("onWorkFinished") private val onWorkFinished: () -> Unit,
    @Assisted("onChangeIcon") private val onChangeIcon: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : ManageCategoriesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(editableCategoryEntityId) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is ManageCategoriesStore.Label.ChooseIcon -> onChangeIcon(it.yetSelectedIconId)
                    is ManageCategoriesStore.Label.ErrorMsg -> _events.emit(Events.CreateCategoryError(it.errorMsg))
                    ManageCategoriesStore.Label.WorkFinished -> onWorkFinished()
                }
            }
        }
    }

    private val _events: MutableSharedFlow<Events> = MutableSharedFlow()
    override val events: SharedFlow<Events> = _events.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ManageCategoriesStore.State> = store.stateFlow


    override fun onChangeCategoryName(name: String) {
        store.accept(Intent.ChangeCategoryName(name))
    }

    override fun onChangeCategoryType(type: CategoryType) {
        store.accept(Intent.ChangeCategoryType(type))
    }

    override fun onClickCreateCategory() {
        store.accept(Intent.ClickSave)
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
    interface Factory : ManageCategoriesComponent.Factory {
        override fun invoke(
            @Assisted("editableCategoryEntityId") editableCategoryEntityId: Long?,
            @Assisted("onWorkFinished") onWorkFinished: () -> Unit,
            @Assisted("onChangeIcon") onChangeIcon: (Long) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultManageCategoriesComponent
    }
}