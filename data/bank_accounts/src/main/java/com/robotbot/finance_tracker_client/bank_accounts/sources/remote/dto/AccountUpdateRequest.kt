package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto

import java.math.BigDecimal

data class AccountUpdateRequest(
    val name: String? = null,
    val currencyCode: String? = null,
    val iconId: Long? = null,
    val balance: BigDecimal? = null
)