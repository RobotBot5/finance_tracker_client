package com.robotbot.finance_tracker_client.bank_accounts.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.entities.TotalBalanceEntity
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore.State
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme
import java.math.BigDecimal

@Composable
fun AccountsContent(component: AccountsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val accountsState = state.accountsState) {
        State.AccountsState.Initial -> {

        }

        is State.AccountsState.Content -> {
            AccountsList(
                accounts = accountsState.accounts,
                totalBalanceState = state.totalBalanceState,
                onAccountClicked = component::onAccountClicked,
                onCreateTransferClicked = component::onCreateTransferClicked,
                onCreateAccountClicked = component::onCreateAccountClicked,
                modifier = modifier
            )
        }

        State.AccountsState.Error -> {
            Text(text = "Error")
        }

        State.AccountsState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun AccountsList(
    accounts: List<AccountEntity>,
    totalBalanceState: State.TotalBalanceState,
    onAccountClicked: (Long) -> Unit,
    onCreateTransferClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateAccountClicked
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Total Balance",
                        style = MaterialTheme.typography.labelLarge
                    )
                    val text = when (totalBalanceState) {
                        State.TotalBalanceState.Initial -> ""
                        State.TotalBalanceState.Loading -> "Loading"
                        State.TotalBalanceState.Error -> "Error"
                        is State.TotalBalanceState.Content -> {
                            "${totalBalanceState.totalBalance.totalBalance.toPlainString()}${totalBalanceState.totalBalance.targetCurrency.symbol}"
                        }
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onCreateTransferClicked,
                        ) {
                            Text(text = "Transfer")
                        }
                        Button(
                            onClick = {}
                        ) { Text(text = "Transfer History") }
                    }
                }
            }
            LazyColumn {
                items(
                    items = accounts,
                    key = { it.id }
                ) {
                    AccountItem(it, onAccountClicked)
                }
            }
        }
    }
}

@Composable
private fun AccountItem(
    account: AccountEntity,
    onAccountClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageLoader = if (LocalInspectionMode.current) {
            ImageLoader.Builder(LocalContext.current).build()
        } else {
            LocalCoilImageLoader.current
        }

        AsyncImage(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            model = BASE_URL.dropLast(1) + account.icon.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${account.balance.toPlainString()}${account.currency.symbol}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onAccountClicked(account.id) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Edit")
        }
    }
}

@Preview("light")
@Composable
private fun AccountsListPreviewLight() {
    val accounts = buildList {
        repeat(5) {
            add(
                AccountEntity(
                    id = it.toLong(),
                    name = "Name: $it",
                    currency = CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
                    balance = BigDecimal.valueOf((it * 100).toLong()),
                    icon = IconEntity(id = 1, name = "name", path = "path")
                )
            )
        }
    }

    FinanceTrackerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AccountsList(
                accounts = accounts,
                totalBalanceState = State.TotalBalanceState.Content(
                    TotalBalanceEntity(
                        totalBalance = BigDecimal.valueOf(1000),
                        targetCurrency = CurrencyEntity(
                            code = "USD",
                            symbol = "$",
                            name = "dollars"
                        )
                    )
                ),
                onAccountClicked = {},
                onCreateTransferClicked = {},
                onCreateAccountClicked = {}
            )
        }
    }
}

@Preview(name = "dark")
@Composable
private fun AccountsListPreviewDark() {
    val accounts = buildList {
        repeat(5) {
            add(
                AccountEntity(
                    id = it.toLong(),
                    name = "Name: $it",
                    currency = CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
                    balance = BigDecimal.valueOf((it * 100).toLong()),
                    icon = IconEntity(id = 1, name = "name", path = "path")
                )
            )
        }
    }

    FinanceTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            AccountsList(
                accounts = accounts,
                totalBalanceState = State.TotalBalanceState.Content(
                    TotalBalanceEntity(
                        totalBalance = BigDecimal.valueOf(1000),
                        targetCurrency = CurrencyEntity(
                            code = "USD",
                            symbol = "$",
                            name = "dollars"
                        )
                    )
                ),
                onAccountClicked = {},
                onCreateTransferClicked = {},
                onCreateAccountClicked = {}
            )
        }
    }
}

@Preview
@Composable
private fun AccountItemPreview() {
    FinanceTrackerTheme {
        Surface {
            AccountItem(
                account = AccountEntity(
                    1,
                    "Account 1",
                    CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
                    BigDecimal.valueOf(2000),
                    IconEntity(id = 1, name = "Wallet", path = "/icons/dasddas")
                ),
                onAccountClicked = {}
            )
        }
    }
}
