package com.robotbot.finance_tracker_client.currency_choose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesComponent

@Composable
fun ChooseCurrencyContent(component: CurrenciesComponent, modifier: Modifier = Modifier) {

    val state = component.model.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (state.value.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(
                items = state.value.currenciesList,
                key = { it.code },
            ) {
                Row(
                    modifier = Modifier
                        .clickable { component.onSelectCurrency(it.code) }
                ) {
                    Text(text = it.name)
                }
            }
        }
    }
}