package com.robotbot.finance_tracker_client.categories.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesStore.Intent
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultCategoriesComponent @AssistedInject constructor(
    private val storeFactory: CategoriesStoreFactory,
    @Assisted("categoryType") private val categoryType: CategoryType,
    @Assisted("onCreateCategory") private val onCreateCategory: () -> Unit,
    @Assisted("onEditCategory") private val onEditCategory: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : CategoriesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(categoryType) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    CategoriesStore.Label.CreateCategoryNavigate -> onCreateCategory()
                    is CategoriesStore.Label.EditCategoryNavigate -> onEditCategory(it.categoryId)
                }
            }
        }
        doOnStart {
            store.accept(Intent.ReloadCategories)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CategoriesStore.State> = store.stateFlow

    override fun onCreateCategoryClicked() {
        store.accept(Intent.OnCreateCategoryClicked)
    }

    override fun onCategoryClicked(categoryId: Long) {
        store.accept(Intent.OnCategoryClicked(categoryId))
    }

    @AssistedFactory
    interface Factory : CategoriesComponent.Factory {
        override fun invoke(
            @Assisted("categoryType") categoryType: CategoryType,
            @Assisted("onCreateCategory") onCreateCategory: () -> Unit,
            @Assisted("onEditCategory") onEditCategory: (Long) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultCategoriesComponent
    }
}
