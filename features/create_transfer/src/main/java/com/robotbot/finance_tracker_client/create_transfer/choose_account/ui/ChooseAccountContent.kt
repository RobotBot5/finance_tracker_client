package com.robotbot.finance_tracker_client.create_transfer.choose_account.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountComponent

@Composable
fun ChooseAccountContent(component: ChooseAccountComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    LazyColumn(
        modifier = Modifier.padding(top = 50.dp)
    ) {
        items(
            items = state.accounts,
            key = { it.id }
        ) { account ->
            Text(
                modifier = Modifier
                    .clickable { component.onAccountClicked(account.id) },
                text = account.name,
                color = if (state.yetSelectedAccountId == account.id) Color.Red else Color.Black
            )
        }
    }
}