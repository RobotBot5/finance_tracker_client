package com.robotbot.finance_tracker_client.create_transfer.choose_account.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountComponent
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme
import java.math.BigDecimal

@Composable
fun ChooseAccountContent(component: ChooseAccountComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    ChooseAccount(
        accounts = state.accounts,
        selectedAccountId = state.yetSelectedAccountId,
        onAccountClicked = component::onAccountClicked,
        modifier = modifier
    )
}

@Composable
private fun ChooseAccount(
    accounts: List<AccountEntity>,
    selectedAccountId: Long?,
    onAccountClicked: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = accounts,
            key = { it.id }
        ) { account ->
            AccountItem(
                account = account,
                isSelected = account.id == selectedAccountId,
                onAccountClicked = onAccountClicked
            )
        }
    }
}

@Composable
private fun AccountItem(
    account: AccountEntity,
    isSelected: Boolean,
    onAccountClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onAccountClicked(account.id) },
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
                .background(
                    if (isSelected)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.primary
                )
                .padding(8.dp),
            model = BASE_URL.dropLast(1) + account.icon.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(
                if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onPrimary
            ),
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
    }
}

@Preview("light")
@Composable
private fun ChooseAccountPreviewLight() {
    val accounts = buildList {
        repeat(10) {
            add(
                AccountEntity(
                    id = it.toLong(),
                    name = "Account $it",
                    currency = CurrencyEntity("USD", "$", "dollars"),
                    balance = BigDecimal.valueOf(it * 100L),
                    icon = IconEntity(1, "", "")
                )
            )
        }
    }

    FinanceTrackerTheme {
        Surface {
            ChooseAccount(
                accounts = accounts,
                selectedAccountId = 1,
                onAccountClicked = {}
            )
        }
    }
}

@Preview("dark")
@Composable
private fun ChooseAccountPreviewDark() {
    val accounts = buildList {
        repeat(10) {
            add(
                AccountEntity(
                    id = it.toLong(),
                    name = "Account $it",
                    currency = CurrencyEntity("USD", "$", "dollars"),
                    balance = BigDecimal.valueOf(it * 100L),
                    icon = IconEntity(1, "", "")
                )
            )
        }
    }

    FinanceTrackerTheme(darkTheme = true) {
        Surface {
            ChooseAccount(
                accounts = accounts,
                selectedAccountId = 1,
                onAccountClicked = {}
            )
        }
    }
}
