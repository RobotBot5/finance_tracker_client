package com.robotbot.finance_tracker_client.transactions.category_choose.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.categories.CategoriesRepository
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseStore.Intent
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseStore.Label
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CategoryChooseStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class SelectCategory(val id: Long) : Intent

        data class ChangeCategoryType(val categoryType: CategoryType) : Intent
    }

    data class State(
        val isLoading: Boolean,
        val categoriesList: List<CategoryEntity>,
        val selectedCategoryType: CategoryType,
        val yetSelectedCategoryId: Long?
    )

    sealed interface Label {

        data class CategorySelected(val categoryId: Long) : Label
    }
}

internal class CategoryChooseStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val categoriesRepository: CategoriesRepository
) {

    fun create(yetSelectedCategoryId: Long?): CategoryChooseStore =
        object : CategoryChooseStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AccountChooseStore",
            initialState = State(
                isLoading = true,
                categoriesList = listOf(),
                selectedCategoryType = CategoryType.EXPENSE,
                yetSelectedCategoryId = yetSelectedCategoryId
            ),
            bootstrapper = BootstrapperImpl(yetSelectedCategoryId),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class CategoriesLoading(val isLoading: Boolean) : Action
        data class CategoriesLoaded(val categories: List<CategoryEntity>) : Action
        data class CategoriesError(val errorMsg: String) : Action
    }

    private sealed interface Msg {

        data class CategoriesLoading(val isLoading: Boolean) : Msg
        data class CategoriesLoaded(val categories: List<CategoryEntity>) : Msg
        data class CategoriesError(val errorMsg: String) : Msg
        data class ChangeCategoryType(val categoryType: CategoryType) : Msg
    }

    private inner class BootstrapperImpl(private val yetSelectedCategoryId: Long?) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.CategoriesLoading(true))
                try {
                    val selectedCategoryType =
                        yetSelectedCategoryId?.let { categoriesRepository.getCategoryById(it) }?.type
                            ?: CategoryType.EXPENSE
                    val categories = categoriesRepository.getCategoriesByType(selectedCategoryType)
                    dispatch(Action.CategoriesLoaded(categories))
                } catch (e: Exception) {
                    dispatch(Action.CategoriesError(e.message ?: "Unknown error"))
                } finally {
                    dispatch(Action.CategoriesLoading(false))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.SelectCategory -> publish(Label.CategorySelected(intent.id))
                is Intent.ChangeCategoryType -> {
                    dispatch(Msg.ChangeCategoryType(intent.categoryType))
                    scope.launch {
                        dispatch(Msg.CategoriesLoading(true))
                        try {
                            val categories =
                                categoriesRepository.getCategoriesByType(intent.categoryType)
                            dispatch(Msg.CategoriesLoaded(categories))
                        } catch (e: Exception) {
                            dispatch(Msg.CategoriesError(e.message ?: "Unknown error"))
                        } finally {
                            dispatch(Msg.CategoriesLoading(false))
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.CategoriesError -> dispatch(Msg.CategoriesError(action.errorMsg))
                is Action.CategoriesLoaded -> dispatch(Msg.CategoriesLoaded(action.categories))
                is Action.CategoriesLoading -> dispatch(Msg.CategoriesLoading(action.isLoading))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.CategoriesError -> copy()
            is Msg.CategoriesLoaded -> copy(categoriesList = msg.categories)
            is Msg.CategoriesLoading -> copy(isLoading = msg.isLoading)
            is Msg.ChangeCategoryType -> copy(selectedCategoryType = msg.categoryType)
        }
    }
}
