package com.robotbot.finance_tracker_client.categories.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.main.RootCategoriesComponent.Child
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

internal class DefaultRootCategoriesComponent @AssistedInject constructor(
    private val categoriesComponentFactory: CategoriesComponent.Factory,
    @Assisted("onCreateCategoryNavigate") private val onCreateCategoryNavigate: () -> Unit,
    @Assisted("onEditCategoryNavigate") private val onEditCategoryNavigate: (Long) -> Unit,
    @Assisted componentContext: ComponentContext
) : RootCategoriesComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.ExpenseCategories,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(config: Config, childComponentContext: ComponentContext): Child =
        when (config) {
            Config.ExpenseCategories -> Child.ExpenseCategories(
                categoriesComponent(
                    CategoryType.EXPENSE,
                    childComponentContext
                )
            )

            Config.IncomeCategories -> Child.IncomeCategories(
                categoriesComponent(
                    CategoryType.INCOME,
                    childComponentContext
                )
            )
        }

    private fun categoriesComponent(
        categoryType: CategoryType,
        componentContext: ComponentContext
    ): CategoriesComponent =
        categoriesComponentFactory(
            categoryType = categoryType,
            onCreateCategory = onCreateCategoryNavigate,
            onEditCategory = onEditCategoryNavigate,
            componentContext = componentContext
        )

    override fun onTabClicked(tab: CategoriesNavTab) {
        when (tab) {
            CategoriesNavTab.Expenses -> navigation.bringToFront(Config.ExpenseCategories)
            CategoriesNavTab.Income -> navigation.bringToFront(Config.IncomeCategories)
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object ExpenseCategories : Config

        @Serializable
        data object IncomeCategories : Config
    }

    @AssistedFactory
    interface Factory : RootCategoriesComponent.Factory {
        override fun invoke(
            @Assisted("onCreateCategoryNavigate") onCreateCategoryNavigate: () -> Unit,
            @Assisted("onEditCategoryNavigate") onEditCategoryNavigate: (Long) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultRootCategoriesComponent
    }
}
