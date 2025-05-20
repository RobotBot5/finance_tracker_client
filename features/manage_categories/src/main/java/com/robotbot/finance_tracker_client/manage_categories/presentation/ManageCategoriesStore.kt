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
import kotlinx.coroutines.Job
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

        data object Reload : Intent
    }

    sealed interface State {

        data object Loading : State

        data object Error : State

        data class Content(
            val categoryName: String,
            val selectedIconEntity: IconEntity?,
            val categoryType: CategoryType,
            val buttonLoading: Boolean,
            val openReason: OpenReason
        ) : State
    }

    sealed interface Label {

        data object WorkFinished : Label

        data class ErrorMsg(val errorMsg: String) : Label

        data class ChooseIcon(val yetSelectedIconId: Long?) : Label
    }
}

internal class ManageCategoriesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val categoriesRepository: CategoriesRepository,
    private val getInfoRepository: GetInfoRepository
) {

    private var editableCategoryLoadingJob: Job? = null

    private var editableCategoryId: Long? = null

    private val openReason: OpenReason
        get() = if (editableCategoryId == null) CREATE else EDIT

    fun create(editableCategoryId: Long?): ManageCategoriesStore {
        this.editableCategoryId = editableCategoryId
        return object : ManageCategoriesStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ManageCategoriesStore",
            initialState = State.Content(
                categoryName = "",
                selectedIconEntity = null,
                categoryType = CategoryType.INCOME,
                buttonLoading = false,
                openReason = openReason
            ),
            bootstrapper = BootstrapperImpl(editableCategoryId),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}
    }

    private sealed interface Action {

        data object StartLoading : Action

        data object Error : Action

        data class EditableCategoryLoaded(val category: CategoryEntity) : Action
    }

    private sealed interface Msg {

        data object StartLoading : Msg

        data object Error : Msg

        data class ChangeCategoryName(val name: String) : Msg

        data class ChangeCategoryType(val categoryType: CategoryType) : Msg

        data class ChangeButtonLoading(val isLoading: Boolean) : Msg

        data class ChangeSelectedIcon(val icon: IconEntity) : Msg

        data class EditableCategoryLoaded(val category: CategoryEntity) : Msg
    }

    private inner class BootstrapperImpl(private val editableCategoryId: Long?) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            editableCategoryId?.let {
                dispatch(Action.StartLoading)
                editableCategoryLoadingJob = scope.launch {
                    try {
                        val editableCategory =
                            categoriesRepository.getCategoryById(editableCategoryId)
                        dispatch(Action.EditableCategoryLoaded(editableCategory))
                    } catch (e: Exception) {
                        dispatch(Action.Error)
                    }
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.EditableCategoryLoaded -> dispatch(Msg.EditableCategoryLoaded(action.category))
                Action.Error -> dispatch(Msg.Error)
                Action.StartLoading -> dispatch(Msg.StartLoading)
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.Reload -> {
                    editableCategoryLoadingJob?.cancel()
                    val editableCategoryId = editableCategoryId ?: return
                    dispatch(Msg.StartLoading)
                    editableCategoryLoadingJob = scope.launch {
                        try {
                            val editableCategory =
                                categoriesRepository.getCategoryById(editableCategoryId)
                            dispatch(Msg.EditableCategoryLoaded(editableCategory))
                        } catch (e: Exception) {
                            dispatch(Msg.Error)
                        }
                    }
                }

                is Intent.ChangeCategoryName -> {
                    dispatch(Msg.ChangeCategoryName(intent.name))
                }

                is Intent.ChangeCategoryType -> {
                    dispatch(Msg.ChangeCategoryType(intent.categoryType))
                }

                Intent.ClickSave -> {
                    val currentState = (state() as? State.Content) ?: return
                    val selectedIconId = currentState.selectedIconEntity?.id ?: run {
                        publish(Label.ErrorMsg("Please select icon"))
                        return
                    }
                    if (currentState.categoryName.isEmpty()) {
                        publish(Label.ErrorMsg("Please enter category name"))
                        return
                    }
                    if (openReason == CREATE) {
                        val categoryCreateRequest = CategoryCreateRequest(
                            name = currentState.categoryName,
                            isExpense = currentState.categoryType == CategoryType.EXPENSE,
                            iconId = selectedIconId
                        )
                        scope.launch {
                            try {
                                dispatch(Msg.ChangeButtonLoading(true))
                                categoriesRepository.createCategory(categoryCreateRequest)
                                publish(Label.WorkFinished)
                            } catch (e: Exception) {
                                publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                            } finally {
                                dispatch(Msg.ChangeButtonLoading(false))
                            }
                        }
                    } else if (openReason == EDIT) {
                        val categoryUpdateRequest = CategoryUpdateRequest(
                            name = currentState.categoryName,
                            iconId = selectedIconId
                        )
                        dispatch(Msg.ChangeButtonLoading(true))
                        scope.launch {
                            try {
                                editableCategoryId?.let {
                                    categoriesRepository.updateCategory(it, categoryUpdateRequest)
                                }
                                publish(Label.WorkFinished)
                            } catch (e: Exception) {
                                publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                            } finally {
                                dispatch(Msg.ChangeButtonLoading(false))
                            }
                        }
                    }
                }

                is Intent.ChangeSelectedIcon -> {
                    dispatch(Msg.ChangeButtonLoading(true))
                    scope.launch {
                        try {
                            val iconEntity = getInfoRepository.getIconById(intent.iconId)
                            dispatch(Msg.ChangeSelectedIcon(iconEntity))
                        } catch (e: Exception) {
                            publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                        } finally {
                            dispatch(Msg.ChangeButtonLoading(false))
                        }
                    }
                }

                Intent.IconClicked -> {
                    val selectedIconId = (state() as? State.Content)?.selectedIconEntity?.id
                    publish(Label.ChooseIcon(selectedIconId))
                }

                Intent.ClickDelete -> {
                    scope.launch {
                        try {
                            dispatch(Msg.ChangeButtonLoading(true))
                            editableCategoryId?.let {
                                categoriesRepository.deleteCategory(it)
                            }
                            publish(Label.WorkFinished)
                        } catch (e: Exception) {
                            publish(Label.ErrorMsg(e.message ?: "Unknown error"))
                        } finally {
                            dispatch(Msg.ChangeButtonLoading(false))
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeCategoryName -> if (this is State.Content) copy(categoryName = msg.name) else this
            is Msg.ChangeCategoryType -> if (this is State.Content) copy(categoryType = msg.categoryType) else this
            is Msg.ChangeButtonLoading -> if (this is State.Content) copy(buttonLoading = msg.isLoading) else this
            is Msg.ChangeSelectedIcon -> if (this is State.Content) copy(selectedIconEntity = msg.icon) else this
            is Msg.EditableCategoryLoaded -> State.Content(
                categoryName = msg.category.name,
                selectedIconEntity = msg.category.icon,
                categoryType = msg.category.type,
                buttonLoading = false,
                openReason = EDIT
            )

            Msg.Error -> State.Error
            Msg.StartLoading -> State.Loading
        }
    }
}
