package com.robotbot.finance_tracker_client.create_transfer.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferComponent

@Composable
fun CreateTransferContent(component: CreateTransferComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    Column {
        TextField(
            value = state.amountFrom.toPlainString(),
            onValueChange = { component.onAmountFromChanged(it) },
            label = { Text(text = "Amount from") }
        )
        TextField(
            value = state.amountTo?.toPlainString() ?: "0",
            onValueChange = { component.onAmountToChanged(it) },
            label = { Text(text = "Amount to") }
        )
        Text(
            modifier = Modifier.clickable { component.onAccountFromClicked() },
            text = state.accountFrom?.name ?: "Не выбран"
        )
        Text(
            modifier = Modifier.clickable { component.onAccountToClicked() },
            text = state.accountTo?.name ?: "Не выбран"
        )
        Button(
            onClick = { component.onCreateTransferClicked() }
        ) {
            Text(text = "Create transfer")
        }
    }
}