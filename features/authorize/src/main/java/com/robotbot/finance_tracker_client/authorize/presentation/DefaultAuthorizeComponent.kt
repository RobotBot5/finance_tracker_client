package com.robotbot.finance_tracker_client.authorize.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeStore.Intent
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeStore.State
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class DefaultAuthorizeComponent @AssistedInject constructor(
    private val storeFactory: AuthorizeStoreFactory,
    @Assisted("onAuthSuccess") private val onAuthSuccess: () -> Unit,
    @Assisted("onRegister") private val onRegister: () -> Unit,
    @Assisted private val componentContext: ComponentContext
) : AuthorizeComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    AuthorizeStore.Label.AuthSuccess -> {
                        onAuthSuccess()
                    }
                    is AuthorizeStore.Label.ErrorMsg -> {
                        _events.emit(AuthorizeComponent.Events.AuthError(it.errorMsg))
                    }
                    AuthorizeStore.Label.OnRegisterNavigate -> onRegister()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<State> = store.stateFlow

    private val _events: MutableSharedFlow<AuthorizeComponent.Events> = MutableSharedFlow()

    override val events: SharedFlow<AuthorizeComponent.Events> = _events.asSharedFlow()

    override fun onClickSignIn() {
        store.accept(Intent.ClickSignIn)
    }

    override fun onChangeEmail(email: String) {
        store.accept(Intent.ChangeEmail(email))
    }

    override fun onChangePassword(password: String) {
        store.accept(Intent.ChangePassword(password))
    }

    override fun onRegisterClicked() {
        store.accept(Intent.OnRegisterClicked)
    }

    @AssistedFactory
    interface Factory : AuthorizeComponent.Factory {
        override fun invoke(
            @Assisted("onAuthSuccess") onAuthSuccess: () -> Unit,
            @Assisted("onRegister") onRegister: () -> Unit,
            componentContext: ComponentContext
        ): DefaultAuthorizeComponent
    }
}