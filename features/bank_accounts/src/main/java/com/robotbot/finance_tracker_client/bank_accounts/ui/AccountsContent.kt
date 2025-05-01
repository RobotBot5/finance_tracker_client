package com.robotbot.finance_tracker_client.bank_accounts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore
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
        AccountsStore.State.AccountsState.Initial -> {

        }

        is AccountsStore.State.AccountsState.Content -> {
            AccountsList(
                accounts = accountsState.accounts,
                onAccountClicked = component::onAccountClicked,
                onCreateTransferClicked = component::onCreateTransferClicked,
                modifier = modifier
            )
        }

        AccountsStore.State.AccountsState.Error -> {
            Text(text = "Error")
        }

        AccountsStore.State.AccountsState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@Composable
private fun AccountsList(
    accounts: List<AccountEntity>,
    onAccountClicked: (Long) -> Unit,
    onCreateTransferClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
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
                Text(
                    text = "100000 руб.",
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
        LazyColumn(modifier = modifier) {
            items(
                items = accounts,
                key = { it.id }
            ) {
                AccountItem(it, onAccountClicked)
            }
            item {
                Button(
                    onClick = {}
                ) {
                    Text(text = "Add Account")
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

@Preview
@Composable
private fun AccountsListPreview() {
    val accounts = buildList {
        repeat(5) {
            add(AccountEntity(
                id = it.toLong(),
                name = "Name: $it",
                currency = CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
                balance = BigDecimal.valueOf((it * 100).toLong()),
                icon = IconEntity(id = 1, name = "name", path = "path")
            ))
        }
    }

    FinanceTrackerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AccountsList(
                accounts = accounts,
                onAccountClicked = {},
                onCreateTransferClicked = {}
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
