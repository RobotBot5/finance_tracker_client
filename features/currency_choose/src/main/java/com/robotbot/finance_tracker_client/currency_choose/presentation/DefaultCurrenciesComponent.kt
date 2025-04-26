package com.robotbot.finance_tracker_client.currency_choose.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesStore.Intent
import com.robotbot.finance_tracker_client.dependencies.util.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class DefaultCurrenciesComponent @AssistedInject constructor(
    private val storeFactory: CurrenciesStoreFactory,
    @Assisted("selectedCurrencyCode") private val selectedCurrencyCode: String,
    @Assisted("onCurrencySelected") private val onCurrencySelected: (String) -> Unit,
    @Assisted componentContext: ComponentContext
) : CurrenciesComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(selectedCurrencyCode) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is CurrenciesStore.Label.CurrencySelected -> {
                        onCurrencySelected(it.currencyCode)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<CurrenciesStore.State> = store.stateFlow

    override fun onSelectCurrency(currencyCode: String) {
        store.accept(Intent.SelectCurrency(currencyCode))
    }

    @AssistedFactory
    interface Factory : CurrenciesComponent.Factory {
        override fun invoke(
            @Assisted("selectedCurrencyCode") selectedCurrencyCode: String,
            @Assisted("onCurrencySelected") onCurrencySelected: (String) -> Unit,
            componentContext: ComponentContext
        ): DefaultCurrenciesComponent
    }
}