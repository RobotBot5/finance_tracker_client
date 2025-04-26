package com.robotbot.finance_tracker_client.bank_accounts.entities

import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import java.math.BigDecimal

data class AccountEntity(
    val id: Long,
    val name: String,
    val currency: CurrencyEntity,
    val balance: BigDecimal,
    val icon: IconEntity
)
