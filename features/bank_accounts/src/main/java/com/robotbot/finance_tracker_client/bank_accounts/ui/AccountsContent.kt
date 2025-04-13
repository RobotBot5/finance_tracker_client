package com.robotbot.finance_tracker_client.bank_accounts.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore

@Composable
fun AccountsContent(component: AccountsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val accountsState = state.accountsState) {
        AccountsStore.State.AccountsState.Initial -> {

        }
        is AccountsStore.State.AccountsState.Content -> {
            LazyColumn {
                items(
                    items = accountsState.accounts,
                    key = { it.id }
                ) {
                    Text(text = it.name)
                }
            }
        }
        AccountsStore.State.AccountsState.Error -> {
            Text(text = "Error")
        }
        AccountsStore.State.AccountsState.Loading -> {
            Text(text = "Loading")
        }
    }
}