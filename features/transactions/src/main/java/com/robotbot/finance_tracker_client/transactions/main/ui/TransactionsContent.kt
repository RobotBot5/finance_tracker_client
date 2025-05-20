package com.robotbot.finance_tracker_client.transactions.main.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsComponent
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsStore.State.GroupedByDateTransactions
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun TransactionsContent(component: TransactionsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val currentState = state.transactionsState) {
        is TransactionsStore.State.TransactionsState.Content -> {
            TransactionsList(
                groupedTransactions = currentState.transactions,
                onTransactionClicked = component::onTransactionClicked,
                onCreateTransactionClicked = component::onCreateTransactionClicked,
                modifier = modifier
            )
        }

        is TransactionsStore.State.TransactionsState.Error -> {
            Text(text = currentState.errorMsg)
        }

        TransactionsStore.State.TransactionsState.Initial -> {
        }

        TransactionsStore.State.TransactionsState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun TransactionsList(
    groupedTransactions: List<GroupedByDateTransactions>,
    onTransactionClicked: (Long) -> Unit,
    onCreateTransactionClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateTransactionClicked
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        LazyColumn {
            groupedTransactions.forEach { group ->
                item {
                    Text(
                        text = group.dateLabel,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(8.dp)
                    )
                }
                items(
                    items = group.transactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onTransactionClicked = onTransactionClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: TransactionEntity,
    onTransactionClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageLoader = if (LocalInspectionMode.current) {
        ImageLoader.Builder(LocalContext.current).build()
    } else {
        LocalCoilImageLoader.current
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .height(56.dp)
            .clickable { onTransactionClicked(transaction.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(8.dp),
            model = BASE_URL.dropLast(1) + transaction.category.icon.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = transaction.category.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = transaction.account.name,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${transaction.amount.toPlainString()}${transaction.account.currency.symbol}"
        )
    }
}

@SuppressLint("NewApi")
@Preview
@Composable
private fun TransactionItemPreview() {
    FinanceTrackerTheme {
        Surface {
            TransactionItem(
                TransactionEntity(
                    id = 1,
                    amount = BigDecimal.valueOf(100L),
                    date = LocalDate.now(),
                    category = CategoryEntity(
                        id = 1,
                        name = "Category",
                        type = CategoryType.EXPENSE,
                        isSystem = false,
                        icon = IconEntity(id = 1, name = "", path = "")
                    ),
                    account = AccountEntity(
                        id = 1,
                        name = "Account",
                        currency = CurrencyEntity(
                            code = "USD",
                            symbol = "$",
                            name = "dollars"
                        ),
                        balance = BigDecimal.valueOf(1000L),
                        icon = IconEntity(id = 1, name = "", path = "")
                    )
                ),
                onTransactionClicked = {}
            )
        }
    }
}

@SuppressLint("NewApi")
@Preview(name = "light")
@Composable
private fun TransactionsListLight() {
    val groupedTransactions = buildList {
        repeat(3) {
            add(
                GroupedByDateTransactions(
                    dateLabel = "${10 * (it + 1)} мая 2025",
                    transactions = buildList {
                        for (i in it * 3..(it * 3 + 2)) {
                            add(
                                TransactionEntity(
                                    id = i.toLong(),
                                    amount = BigDecimal.valueOf(i * 100L),
                                    date = LocalDate.now(),
                                    category = CategoryEntity(
                                        id = i.toLong(),
                                        name = "Category: $i",
                                        type = CategoryType.EXPENSE,
                                        isSystem = false,
                                        icon = IconEntity(id = 1, name = "", path = "")
                                    ),
                                    account = AccountEntity(
                                        id = i.toLong(),
                                        name = "Account: $i",
                                        currency = CurrencyEntity(
                                            code = "USD",
                                            symbol = "$",
                                            name = "dollars"
                                        ),
                                        balance = BigDecimal.valueOf(i * 1000L),
                                        icon = IconEntity(id = 1, name = "", path = "")
                                    )
                                )
                            )
                        }
                    }
                )
            )
        }
    }

    FinanceTrackerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TransactionsList(
                groupedTransactions = groupedTransactions,
                onTransactionClicked = {},
                onCreateTransactionClicked = {}
            )
        }
    }
}

@SuppressLint("NewApi")
@Preview(name = "dark")
@Composable
private fun TransactionsListDark() {
    val groupedTransactions = buildList {
        repeat(3) {
            add(
                GroupedByDateTransactions(
                    dateLabel = "${10 * (it + 1)} мая 2025",
                    transactions = buildList {
                        for (i in it * 3..(it * 3 + 2)) {
                            add(
                                TransactionEntity(
                                    id = i.toLong(),
                                    amount = BigDecimal.valueOf(i * 100L),
                                    date = LocalDate.now(),
                                    category = CategoryEntity(
                                        id = i.toLong(),
                                        name = "Category: $i",
                                        type = CategoryType.EXPENSE,
                                        isSystem = false,
                                        icon = IconEntity(id = 1, name = "", path = "")
                                    ),
                                    account = AccountEntity(
                                        id = i.toLong(),
                                        name = "Account: $i",
                                        currency = CurrencyEntity(
                                            code = "USD",
                                            symbol = "$",
                                            name = "dollars"
                                        ),
                                        balance = BigDecimal.valueOf(i * 1000L),
                                        icon = IconEntity(id = 1, name = "", path = "")
                                    )
                                )
                            )
                        }
                    }
                )
            )
        }
    }

    FinanceTrackerTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            TransactionsList(
                groupedTransactions = groupedTransactions,
                onTransactionClicked = {},
                onCreateTransactionClicked = {}
            )
        }
    }
}
