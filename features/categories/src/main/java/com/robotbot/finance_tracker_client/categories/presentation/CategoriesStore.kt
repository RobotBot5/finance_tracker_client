package com.robotbot.finance_tracker_client.categories.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.categories.CategoriesRepository
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesStore.Intent
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesStore.Label
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesStore.State
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesStore.State.CategoriesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface CategoriesStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ReloadCategories : Intent

        data object OnCreateCategoryClicked : Intent

        data class OnCategoryClicked(val categoryId: Long) : Intent
    }

    data class State(val categoriesState: CategoriesState) {

        sealed interface CategoriesState {

            data object Initial : CategoriesState
            data object Loading : CategoriesState
            data object Error : CategoriesState
            data class Content(
                val categories: List<CategoryEntity>
            ) : CategoriesState
        }
    }

    sealed interface Label {

        data object CreateCategoryNavigate : Label

        data class EditCategoryNavigate(val categoryId: Long) : Label
    }
}

internal class CategoriesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val categoriesRepository: CategoriesRepository
) {

    private var loadCategoriesJob: Job? = null

    fun create(categoryType: CategoryType): CategoriesStore =
        object : CategoriesStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CategoriesStore",
            initialState = State(CategoriesState.Initial),
            bootstrapper = BootstrapperImpl(categoryType),
            executorFactory = { ExecutorImpl(categoryType) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data object CategoriesLoading : Action

        data object CategoriesError : Action

        data class CategoriesContent(val categories: List<CategoryEntity>) : Action
    }

    private sealed interface Msg {

        data object CategoriesLoading : Msg

        data object CategoriesError : Msg

        data class CategoriesContent(val categories: List<CategoryEntity>) : Msg
    }

    private inner class BootstrapperImpl(private val categoryType: CategoryType) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.CategoriesLoading)
            loadCategoriesJob = scope.launch {
                try {
                    val categories = categoriesRepository.getCategoriesByType(categoryType)
                    dispatch(Action.CategoriesContent(categories))
                }
                catch (e: Exception) {
                    dispatch(Action.CategoriesError)
                }
            }
        }
    }

    private inner class ExecutorImpl(private val categoryType: CategoryType) :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.CategoriesContent -> dispatch(Msg.CategoriesContent(action.categories))
                Action.CategoriesError -> dispatch(Msg.CategoriesError)
                Action.CategoriesLoading -> dispatch(Msg.CategoriesLoading)
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.ReloadCategories -> {
                    loadCategoriesJob?.cancel()
                    dispatch(Msg.CategoriesLoading)
                    loadCategoriesJob = scope.launch {
                        try {
                            val categories = categoriesRepository.getCategoriesByType(categoryType)
                            dispatch(Msg.CategoriesContent(categories))
                        }
                        catch (e: Exception) {
                            dispatch(Msg.CategoriesError)
                        }
                    }
                }

                is Intent.OnCategoryClicked -> publish(Label.EditCategoryNavigate(intent.categoryId))
                Intent.OnCreateCategoryClicked -> publish(Label.CreateCategoryNavigate)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.CategoriesContent -> copy(categoriesState = CategoriesState.Content(msg.categories))
            Msg.CategoriesError -> copy(categoriesState = CategoriesState.Error)
            Msg.CategoriesLoading -> copy(categoriesState = CategoriesState.Loading)
        }
    }
}
