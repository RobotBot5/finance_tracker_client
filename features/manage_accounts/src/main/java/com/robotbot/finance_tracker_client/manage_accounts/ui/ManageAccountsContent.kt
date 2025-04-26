package com.robotbot.finance_tracker_client.manage_accounts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsComponent
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsStore
import com.robotbot.finance_tracker_client.manage_accounts.presentation.OpenReason.EDIT
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader

@Composable
fun ManageAccountsContent(component: ManageAccountsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    ManageAccounts(
        state = state,
        onAccountTitleChange = component::onChangeAccountTitle,
        onBalanceChange = component::onChangeBalance,
        onCreateAccountClicked = component::onClickCreateAccount,
        onChangeCurrencyClicked = component::onChangeCurrency,
        onIconClicked = component::onIconClicked,
        onDeleteAccountClicked = component::onDeleteClicked,
        modifier = modifier
    )
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

    val imageLoader = LocalCoilImageLoader.current

    Column(
        modifier = modifier
            .fillMaxSize(),
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
        TextField(
            value = state.balance.toString(),
            onValueChange = onBalanceChange,
            label = {
                Text(text = "Balance")
            },
        )
        Text(
            modifier = Modifier.clickable { onChangeCurrencyClicked() },
            text = state.selectedCurrency.code
        )
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
        Button(
            onClick = onCreateAccountClicked,
        ) {
            Text(text = "Create Account")
        }
        if (state.openReason == EDIT) {
            Button(
                onClick = { onDeleteAccountClicked() }
            ) {
                Text(text = "Delete")
            }
        }
    }
}

//@Preview
//@Composable
//private fun ManageAccountsPreview() {
//    FinanceTrackerTheme {
//        Surface {
//            ManageAccounts(
//                state = ManageAccountsStore.State(
//                    accountTitle = "",
//                    balance = BigDecimal.ZERO,
//                    isLoading = false,
//                    selectedCurrency = CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
//                    openReason = EDIT,
//                    selectedIconEntity = IconEntity(
//                        id = 1,
//                        name = "dasd",
//                        path = "asdsad"
//                    )
//                ),
//                onBalanceChange = {},
//                onAccountTitleChange = {},
//                onCreateAccountClicked = {},
//                onChangeCurrencyClicked = {},
//                onIconClicked = {}
//            )
//        }
//    }
//}