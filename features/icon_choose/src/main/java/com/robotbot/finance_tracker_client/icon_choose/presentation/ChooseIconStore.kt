package com.robotbot.finance_tracker_client.icon_choose.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.robotbot.finance_tracker_client.get_info.GetInfoRepository
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconStore.Intent
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconStore.Label
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ChooseIconStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class SelectIcon(val id: Long) : Intent
    }

    data class State(
        val isLoading: Boolean,
        val iconsList: List<IconEntity>,
        val yetSelectedIconId: Long?
    )

    sealed interface Label {

        data class IconSelected(val iconId: Long) : Label
    }
}

internal class ChooseIconsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getInfoRepository: GetInfoRepository
) {

    fun create(yetSelectedIconId: Long?): ChooseIconStore =
        object : ChooseIconStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ChooseIconsStore",
            initialState = State(
                isLoading = true,
                iconsList = listOf(),
                yetSelectedIconId = yetSelectedIconId
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class IconsLoading(val isLoading: Boolean) : Action
        data class IconsLoaded(val icons: List<IconEntity>) : Action
        data class IconsError(val errorMsg: String) : Action
    }

    private sealed interface Msg {

        data class IconsLoading(val isLoading: Boolean) : Msg
        data class IconsLoaded(val icons: List<IconEntity>) : Msg
        data class IconsError(val errorMsg: String) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.IconsLoading(true))
                try {
                    val iconsList = getInfoRepository.getIconsList()
                    dispatch(Action.IconsLoaded(iconsList))
                } catch (e: Exception) {
                    dispatch(Action.IconsError(e.message ?: "Unknown error"))
                } finally {
                    dispatch(Action.IconsLoading(false))
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.SelectIcon -> publish(Label.IconSelected(intent.id))
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.IconsError -> dispatch(Msg.IconsError(action.errorMsg))
                is Action.IconsLoaded -> dispatch(Msg.IconsLoaded(action.icons))
                is Action.IconsLoading -> dispatch(Msg.IconsLoading(action.isLoading))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.IconsError -> copy()
            is Msg.IconsLoaded -> copy(iconsList = msg.icons)
            is Msg.IconsLoading -> copy(isLoading = msg.isLoading)
        }
    }
}
