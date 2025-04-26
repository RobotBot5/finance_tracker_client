package com.robotbot.finance_tracker_client.currency_choose.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface CurrenciesComponent {

    val model: StateFlow<CurrenciesStore.State>

    fun onSelectCurrency(currencyCode: String)

    fun interface Factory {
        operator fun invoke(
            selectedCurrencyCode: String,
            onCurrencySelected: (String) -> Unit,
            componentContext: ComponentContext
        ): CurrenciesComponent
    }
}