package com.robotbot.finance_tracker_client.currency_choose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesComponent
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun ChooseCurrencyContent(component: CurrenciesComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        CurrenciesList(
            currencies = state.currenciesList,
            onSelectCurrency = component::onSelectCurrency,
            selectedCurrencyCode = state.selectedCurrencyCode,
            modifier = modifier
        )
    }
}

@Composable
private fun CurrenciesList(
    currencies: List<CurrencyEntity>,
    selectedCurrencyCode: String,
    onSelectCurrency: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = currencies,
            key = { it.code },
        ) {
            Row(
                modifier = Modifier
                    .clickable { onSelectCurrency(it.code) }
                    .fillParentMaxWidth()
                    .height(60.dp)
                    .background(
                        if (it.code == selectedCurrencyCode)
                            MaterialTheme.colorScheme.surfaceVariant
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = it.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = it.code,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(name = "light")
@Composable
private fun CurrenciesListPreviewLight() {
    val currencies = buildList {
        repeat(10) {
            add(
                CurrencyEntity(
                    code = "code: $it",
                    symbol = "RUB",
                    name = "name: $it"
                )
            )
        }
    }

    FinanceTrackerTheme {
        Surface {
            CurrenciesList(
                currencies = currencies,
                selectedCurrencyCode = "code: 5",
                onSelectCurrency = {}
            )
        }
    }
}

@Preview(name = "dark")
@Composable
private fun CurrenciesListPreviewDark() {
    val currencies = buildList {
        repeat(10) {
            add(
                CurrencyEntity(
                    code = "code: $it",
                    symbol = it.toString(),
                    name = "name: $it"
                )
            )
        }
    }

    FinanceTrackerTheme(darkTheme = true) {
        Surface {
            CurrenciesList(
                currencies = currencies,
                selectedCurrencyCode = "code: 5",
                onSelectCurrency = {}
            )
        }
    }
}
