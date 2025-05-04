package com.robotbot.finance_tracker_client.transactions.category_choose.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseStore.Intent
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseStore.Label
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultCategoryChooseComponent @AssistedInject constructor(
    private val storeFactory: CategoryChooseStoreFactory,
    @Assisted("yetSelectedCategoryId") private val yetSelectedCategoryId: Long?,
    @Assisted("onCategorySelected") private val onCategorySelected: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : CategoryChooseComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(yetSelectedCategoryId) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is Label.CategorySelected -> {
                        onCategorySelected(it.categoryId)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CategoryChooseStore.State> = store.stateFlow

    override fun onCategoryClicked(categoryId: Long) {
        store.accept(Intent.SelectCategory(categoryId))
    }

    override fun onChangeCategoryType(categoryType: CategoryType) {
        store.accept(Intent.ChangeCategoryType(categoryType))
    }

    @AssistedFactory
    interface Factory : CategoryChooseComponent.Factory {
        override fun invoke(
            @Assisted("yetSelectedCategoryId") yetSelectedCategoryId: Long?,
            @Assisted("onCategorySelected") onCategorySelected: (Long) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultCategoryChooseComponent
    }
}
