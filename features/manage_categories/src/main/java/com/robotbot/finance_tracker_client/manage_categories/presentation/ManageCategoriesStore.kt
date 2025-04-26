package com.robotbot.finance_tracker_client.manage_categories.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.categories.CategoriesRepository
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryCreateRequest
import com.robotbot.finance_tracker_client.categories.sources.remote.dto.CategoryUpdateRequest
import com.robotbot.finance_tracker_client.get_info.GetInfoRepository
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesStore.Intent
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesStore.Label
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesStore.State
import com.robotbot.finance_tracker_client.manage_categories.presentation.OpenReason.CREATE
import com.robotbot.finance_tracker_client.manage_categories.presentation.OpenReason.EDIT
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ManageCategoriesStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeCategoryName(val name: String) : Intent

        data class ChangeSelectedIcon(val iconId: Long) : Intent

        data class ChangeCategoryType(val categoryType: CategoryType) : Intent

        data object IconClicked : Intent

        data object ClickSave : Intent

        data object ClickDelete : Intent
    }

    data class State(
        val categoryName: String,
        val selectedIconEntity: IconEntity,
        val categoryType: CategoryType,
        val openReason: OpenReason,
        val editableCategoryId: Long?,
        val isLoading: Boolean
    )

    sealed interface Label {

        data object WorkFinished : Label

        data class ErrorMsg(val errorMsg: String) : Label

        data class ChooseIcon(val yetSelectedIconId: Long) : Label
    }
}

internal class ManageCategoriesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val categoriesRepository: CategoriesRepository,
    private val getInfoRepository: GetInfoRepository
) {

    fun create(editableCategoryId: Long?): ManageCategoriesStore =
        object : ManageCategoriesStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ManageCategoriesStore",
            initialState = State(
                categoryName = "",
                selectedIconEntity = IconEntity(id = 1, name = "dasd", path = "/icons/account_card_24dp.svg"),
                openReason = if (editableCategoryId == null) CREATE else EDIT,
                editableCategoryId = editableCategoryId,
                isLoading = true,
                categoryType = CategoryType.EXPENSE
            ),
            bootstrapper = BootstrapperImpl(editableCategoryId),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class EditableCategoryLoaded(val category: CategoryEntity) : Action
    }

    private sealed interface Msg {

        data class ChangeCategoryName(val name: String) : Msg

        data class ChangeCategoryType(val categoryType: CategoryType) : Msg

        data class ChangeLoading(val isLoading: Boolean) : Msg

        data class ChangeSelectedIcon(val icon: IconEntity) : Msg

        data class EditableCategoryLoaded(val category: CategoryEntity) : Msg
    }

    private inner class BootstrapperImpl(private val editableCategoryId: Long?) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            editableCategoryId?.let {
                scope.launch {
                    val editableCategory = categoriesRepository.getCategoryById(editableCategoryId)
                    dispatch(Action.EditableCategoryLoaded(editableCategory))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.EditableCategoryLoaded -> dispatch(Msg.EditableCategoryLoaded(action.category))
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeCategoryName -> {
                    dispatch(Msg.ChangeCategoryName(intent.name))
                }
                is Intent.ChangeCategoryType -> {
                    dispatch(Msg.ChangeCategoryType(intent.categoryType))
                }
                Intent.ClickSave -> {
                    val currentState = state()
                    if (currentState.openReason == CREATE) {
                        val categoryCreateRequest = CategoryCreateRequest(
                            name = currentState.categoryName,
                            isExpense = currentState.categoryType == CategoryType.EXPENSE,
                            iconId = currentState.selectedIconEntity.id
                        )
                        scope.launch {
                            try {
                                dispatch(Msg.ChangeLoading(true))
                                categoriesRepository.createCategory(categoryCreateRequest)
                                publish(Label.WorkFinished)
                            } catch (e: Exception) {
                                publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                            } finally {
                                dispatch(Msg.ChangeLoading(false))
                            }
                        }
                    } else if (currentState.openReason == EDIT) {
                        val categoryUpdateRequest = CategoryUpdateRequest(
                            name = currentState.categoryName,
                            iconId = currentState.selectedIconEntity.id
                        )
                        scope.launch {
                            try {
                                dispatch(Msg.ChangeLoading(true))
                                currentState.editableCategoryId?.let {
                                    categoriesRepository.updateCategory(it, categoryUpdateRequest)
                                }
                                publish(Label.WorkFinished)
                            } catch (e: Exception) {
                                publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                            } finally {
                                dispatch(Msg.ChangeLoading(false))
                            }
                        }
                    }
                }
                is Intent.ChangeSelectedIcon -> {
                    scope.launch {
                        val iconEntity = getInfoRepository.getIconById(intent.iconId)
                        dispatch(Msg.ChangeSelectedIcon(iconEntity))
                    }
                }
                Intent.IconClicked -> publish(Label.ChooseIcon(state().selectedIconEntity.id))
                Intent.ClickDelete -> {
                    scope.launch {
                        try {
                            dispatch(Msg.ChangeLoading(true))
                            state().editableCategoryId?.let {
                                categoriesRepository.deleteCategory(it)
                            }
                            publish(Label.WorkFinished)
                        } catch (e: Exception) {
                            publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                        } finally {
                            dispatch(Msg.ChangeLoading(false))
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeCategoryName -> copy(categoryName = msg.name)
            is Msg.ChangeCategoryType  -> copy(categoryType = msg.categoryType)
            is Msg.ChangeLoading -> copy(isLoading = msg.isLoading)
            is Msg.ChangeSelectedIcon -> copy(selectedIconEntity = msg.icon)
            is Msg.EditableCategoryLoaded -> copy(
                categoryName = msg.category.name,
                selectedIconEntity = msg.category.icon,
                categoryType = msg.category.type
            )
        }
    }
}
