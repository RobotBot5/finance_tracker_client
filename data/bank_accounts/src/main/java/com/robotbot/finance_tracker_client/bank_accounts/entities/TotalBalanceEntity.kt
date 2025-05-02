package com.robotbot.finance_tracker_client.bank_accounts.entities

import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import java.math.BigDecimal

data class TotalBalanceEntity(
    val totalBalance: BigDecimal,
    val targetCurrency: CurrencyEntity
)
