package com.robotbot.finance_tracker_client.transactions.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsComponent
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore

@Composable
fun TransactionsContent(component: TransactionsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val currentState = state.transactionsState) {
        is TransactionsStore.State.TransactionsState.Content -> {
            LazyColumn {
                items(
                    items = currentState.transactions,
                    key = { it.id }
                ) {
                    Text(
                        modifier = Modifier.clickable { component.onTransactionClicked(it.id) },
                        text = it.account.name
                    )
                }
                item {
                    Button(
                        onClick = { component.onCreateTransactionClicked() }
                    ) {
                        Text(text = "Create Transaction")
                    }
                }
            }
        }
        is TransactionsStore.State.TransactionsState.Error -> {
            Text(text = currentState.errorMsg)
        }
        TransactionsStore.State.TransactionsState.Initial -> {
        }
        TransactionsStore.State.TransactionsState.Loading -> {
            Text(text = "Loading")
        }
    }
}