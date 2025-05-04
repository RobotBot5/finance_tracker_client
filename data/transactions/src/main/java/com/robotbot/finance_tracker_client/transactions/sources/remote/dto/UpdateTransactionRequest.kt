package com.robotbot.finance_tracker_client.transactions.sources.remote.dto

import java.math.BigDecimal
import java.time.LocalDate

data class UpdateTransactionRequest(
    val amount: BigDecimal? = null,
    val date: LocalDate? = null,
    val categoryId: Long? = null,
    val accountId: Long? = null
)
