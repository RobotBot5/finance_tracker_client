package com.robotbot.finance_tracker_client.transactions.entities

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import java.math.BigDecimal
import java.time.OffsetDateTime

data class TransactionEntity(
    val id: Long,
    val amount: BigDecimal,
    val time: OffsetDateTime,
    val category: CategoryEntity,
    val account: AccountEntity
)
