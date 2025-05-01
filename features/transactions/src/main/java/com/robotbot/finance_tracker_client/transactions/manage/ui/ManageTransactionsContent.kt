package com.robotbot.finance_tracker_client.transactions.manage.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsComponent

@Composable
fun ManageTransactionsContent(component: ManageTransactionsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    Column {
        TextField(
            value = state.amount.toPlainString(),
            onValueChange = { component.onChangeAmount(it) }
        )
        TextField(
            value = state.time.toString(),
            onValueChange = { component.onChangeTime(it) }
        )
        Text(
            modifier = Modifier.clickable { component.onAccountClicked() },
            text = state.account?.name ?: "Счет"
        )
        Text(
            modifier = Modifier.clickable { component.onCategoryClicked() },
            text = state.category?.name ?: "Категория"
        )
        Button(
            onClick = { component.onSaveClicked() }
        ) { Text(text = "Save") }
        if (state.editableTransactionEntityId != null) {
            Button(
                onClick = { component.onDeleteClicked() }
            ) { Text(text = "Delete") }
        }
    }
}