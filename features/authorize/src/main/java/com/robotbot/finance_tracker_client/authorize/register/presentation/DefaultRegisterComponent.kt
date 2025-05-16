package com.robotbot.finance_tracker_client.authorize.register.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterComponent.Events.ErrorToast
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterStore.Intent
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

internal class DefaultRegisterComponent @AssistedInject constructor(
    private val storeFactory: RegisterStoreFactory,
    @Assisted("onRegisterSuccess") private val onRegisterSuccess: () -> Unit,
    @Assisted componentContext: ComponentContext
) : RegisterComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    RegisterStore.Label.SuccessRegistered -> onRegisterSuccess()
                    RegisterStore.Label.ErrorRegistered -> _events.emit(ErrorToast)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<RegisterStore.State> = store.stateFlow

    private val _events = MutableSharedFlow<RegisterComponent.Events>()
    override val events: SharedFlow<RegisterComponent.Events> = _events.asSharedFlow()

    override fun onChangeEmail(email: String) {
        store.accept(Intent.EmailChanged(email))
    }

    override fun onChangeFirstName(firstName: String) {
        store.accept(Intent.FirstNameChanged(firstName))
    }

    override fun onChangePassword(password: String) {
        store.accept(Intent.PasswordChanged(password))
    }

    override fun onChangeConfirmPassword(confirmPassword: String) {
        store.accept(Intent.PasswordConfirmChanged(confirmPassword))
    }

    override fun onRegisterClicked() {
        store.accept(Intent.RegisterButtonClicked)
    }

    @AssistedFactory
    interface Factory : RegisterComponent.Factory {
        override fun invoke(
            @Assisted("onRegisterSuccess") onRegisterSuccess: () -> Unit,
            @Assisted componentContext: ComponentContext
        ) : DefaultRegisterComponent
    }
}