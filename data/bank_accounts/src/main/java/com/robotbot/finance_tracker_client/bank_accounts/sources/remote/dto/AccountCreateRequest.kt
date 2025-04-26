package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto

import java.math.BigDecimal

data class AccountCreateRequest(
    val name: String,
    val currencyCode: String,
    val iconId: Long,
    val balance: BigDecimal? = null
)
