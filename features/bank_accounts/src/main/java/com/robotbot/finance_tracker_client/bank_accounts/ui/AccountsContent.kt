package com.robotbot.finance_tracker_client.bank_accounts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.svg.SvgDecoder
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.bank_accounts.entities.IconEntity
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsStore
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
            LazyColumn {
                items(
                    items = accountsState.accounts,
                    key = { it.id }
                ) {
                    AccountItem(it)
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

@Composable
private fun AccountItem(account: AccountEntity, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageLoader = LocalCoilImageLoader.current

        AsyncImage(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(8.dp),
            model = BASE_URL.dropLast(1) + account.icon.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
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
                text = account.balance.toPlainString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {}
        ) {
            Text(text = "Edit")
        }
    }
}

@Preview
@Composable
private fun AccountItemPreview() {
    FinanceTrackerTheme {
        Surface {
            AccountItem(account = AccountEntity(
                1,
                "Account 1",
                CurrencyEntity(code = "USD", symbol = "$", name = "dollars"),
                BigDecimal.valueOf(2000),
                IconEntity(name = "Wallet", "wallet")
            ))
        }
    }
}