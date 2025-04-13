package com.robotbot.finance_tracker_client.bank_accounts.entities

import java.math.BigDecimal

data class AccountEntity(
    val id: Long,
    val name: String,
    val currency: CurrencyEntity,
    val balance: BigDecimal,
    val icon: IconEntity
)
