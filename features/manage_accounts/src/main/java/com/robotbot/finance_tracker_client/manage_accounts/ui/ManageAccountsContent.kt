package com.robotbot.finance_tracker_client.manage_accounts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsComponent
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsStore
import com.robotbot.finance_tracker_client.manage_accounts.presentation.OpenReason.EDIT
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun ManageAccountsContent(component: ManageAccountsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        component.events.collect {
            when (it) {
                is ManageAccountsComponent.Events.CreateAccountError ->
                    snackBarHostState.showSnackbar(it.msg)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddings ->
        ManageAccounts(
            state = state,
            onAccountTitleChange = component::onChangeAccountTitle,
            onBalanceChange = component::onChangeBalance,
            onCreateAccountClicked = component::onClickCreateAccount,
            onChangeCurrencyClicked = component::onChangeCurrency,
            onIconClicked = component::onIconClicked,
            onDeleteAccountClicked = component::onDeleteClicked,
            modifier = Modifier.padding(paddings)
        )
    }

}

@Composable
private fun ManageAccounts(
    state: ManageAccountsStore.State,
    onAccountTitleChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onCreateAccountClicked: () -> Unit,
    onChangeCurrencyClicked: () -> Unit,
    onIconClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val imageLoader = if (LocalInspectionMode.current) {
        ImageLoader.Builder(LocalContext.current).build()
    } else {
        LocalCoilImageLoader.current
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = state.accountTitle,
            onValueChange = onAccountTitleChange,
            label = {
                Text(text = "Account Title")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            value = state.balance.toString(),
            onValueChange = onBalanceChange,
            label = {
                Text(text = "Balance")
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(0.7f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.clickable { onChangeCurrencyClicked() },
                text = state.selectedCurrency?.name?.let { name ->
                    "Currency: $name"
                } ?: "Choose currency",
                style = MaterialTheme.typography.labelLarge
            )
            state.selectedIconEntity?.path?.let { path ->
                AsyncImage(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                        .clickable { onIconClicked() },
                    model = BASE_URL.dropLast(1) + state.selectedIconEntity.path,
                    imageLoader = imageLoader,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                    contentDescription = null
                )
            } ?: Icon(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
                    .clickable { onIconClicked() },
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (state.openReason == EDIT) {
            Row(
                modifier = Modifier.fillMaxWidth(0.7f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onCreateAccountClicked,
                ) {
                    Text(text = "Edit Account")
                }
                Button(
                    onClick = onDeleteAccountClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = "Delete Account")
                }
            }
        } else {
            Button(
                onClick = onCreateAccountClicked,
            ) {
                Text(text = "Create Account")
            }
        }
    }
}

@Preview(name = "light")
@Composable
private fun ManageAccountsPreviewLight() {
    FinanceTrackerTheme {
        Surface {
            ManageAccounts(
                state = ManageAccountsStore.State(
                    accountTitle = "",
                    balance = "",
                    isLoading = false,
                    selectedCurrency = null,
                    openReason = EDIT,
                    editableAccountId = null,
                    selectedIconEntity = null
                ),
                onBalanceChange = {},
                onAccountTitleChange = {},
                onCreateAccountClicked = {},
                onChangeCurrencyClicked = {},
                onIconClicked = {},
                onDeleteAccountClicked = {}
            )
        }
    }
}

@Preview(name = "dark")
@Composable
private fun ManageAccountsPreviewDark() {
    FinanceTrackerTheme(darkTheme = true) {
        Surface {
            ManageAccounts(
                state = ManageAccountsStore.State(
                    accountTitle = "",
                    balance = "",
                    isLoading = false,
                    selectedCurrency = CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
                    openReason = EDIT,
                    editableAccountId = null,
                    selectedIconEntity = IconEntity(
                        id = 1,
                        name = "dasd",
                        path = "asdsad"
                    )
                ),
                onBalanceChange = {},
                onAccountTitleChange = {},
                onCreateAccountClicked = {},
                onChangeCurrencyClicked = {},
                onIconClicked = {},
                onDeleteAccountClicked = {}
            )
        }
    }
}
