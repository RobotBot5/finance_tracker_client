package com.robotbot.finance_tracker_client.categories.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
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
    @Assisted("onCreateAccount") private val onCreateAccount: () -> Unit,
    @Assisted("onEditAccount") private val onEditAccount: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : CategoriesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    CategoriesStore.Label.CreateCategoryNavigate -> onCreateAccount()
                    is CategoriesStore.Label.EditCategoryNavigate -> onEditAccount(it.categoryId)
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

    override fun onCategoryClicked(accountId: Long) {
        store.accept(Intent.OnCategoryClicked(accountId))
    }

    @AssistedFactory
    interface Factory : CategoriesComponent.Factory {
        override fun invoke(
            @Assisted("onCreateAccount") onCreateAccount: () -> Unit,
            @Assisted("onEditAccount") onEditAccount: (Long) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultCategoriesComponent
    }
}