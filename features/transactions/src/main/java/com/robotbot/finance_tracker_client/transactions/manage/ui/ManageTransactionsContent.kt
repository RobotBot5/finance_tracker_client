package com.robotbot.finance_tracker_client.transactions.manage.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robotbot.common.toFullString
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsComponent
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsStore
import com.robotbot.finance_tracker_client.transactions.manage.presentation.OpenReason
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun ManageTransactionsContent(
    component: ManageTransactionsComponent,
    modifier: Modifier = Modifier
) {

    val state by component.model.collectAsState()

    ManageTransactions(
        state = state,
        onChangeAmount = component::onChangeAmount,
        onChangeDate = component::onChangeDate,
        onAccountClicked = component::onAccountClicked,
        onCategoryClicked = component::onCategoryClicked,
        onSaveClicked = component::onSaveClicked,
        onDeleteClicked = component::onDeleteClicked,
        modifier = modifier
    )
}

@Composable
private fun ManageTransactions(
    state: ManageTransactionsStore.State,
    onChangeAmount: (String) -> Unit,
    onChangeDate: (LocalDate) -> Unit,
    onAccountClicked: () -> Unit,
    onCategoryClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = state.amount.toPlainString(),
            onValueChange = onChangeAmount,
            label = { Text("Amount") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.date.toFullString(),
            onValueChange = { },
            label = { Text("Date") },
            placeholder = { Text("YYYY/MM/DD") },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            readOnly = true,
            modifier = Modifier
                .pointerInput(state.date) {
                    awaitEachGesture {
                        // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                        // in the Initial pass to observe events before the text field consumes them
                        // in the Main pass.
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showModal = true
                        }
                    }
                }
        )
        if (showModal) {
            DatePickerModal(
                onDateSelected = { it?.let(onChangeDate) },
                onDismiss = { showModal = false }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier
                .width(280.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.clickable { onAccountClicked() },
                text = state.account?.name ?: "Choose account"
            )
            Text(
                modifier = Modifier.clickable { onCategoryClicked() },
                text = state.category?.name ?: "Choose category"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (state.editableTransactionEntityId == null) {
            Button(
                onClick = onSaveClicked
            ) {
                Text(text = "Save")
            }
        } else {
            Row(
                modifier = Modifier
                    .width(280.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onSaveClicked
                ) {
                    Text(text = "Edit")
                }
                Button(
                    onClick = onDeleteClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis?.let {
                    LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                })
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@SuppressLint("NewApi")
@Preview(name = "light")
@Composable
private fun ManageTransactionsLight() {
    FinanceTrackerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ManageTransactions(
                state = ManageTransactionsStore.State(
                    amount = BigDecimal.ZERO,
                    date = LocalDate.now(),
                    category = null,
                    account = null,
                    openReason = OpenReason.ADD,
                    editableTransactionEntityId = 1,
                    isLoading = false
                ),
                onChangeAmount = {},
                onChangeDate = {},
                onAccountClicked = {},
                onCategoryClicked = {},
                onSaveClicked = {},
                onDeleteClicked = {},
            )
        }
    }
}

@SuppressLint("NewApi")
@Preview(name = "dark")
@Composable
private fun ManageTransactionsDark() {
    FinanceTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ManageTransactions(
                state = ManageTransactionsStore.State(
                    amount = BigDecimal.ZERO,
                    date = LocalDate.now(),
                    category = null,
                    account = null,
                    openReason = OpenReason.ADD,
                    editableTransactionEntityId = null,
                    isLoading = false
                ),
                onChangeAmount = {},
                onChangeDate = {},
                onAccountClicked = {},
                onCategoryClicked = {},
                onSaveClicked = {},
                onDeleteClicked = {},
            )
        }
    }
}
