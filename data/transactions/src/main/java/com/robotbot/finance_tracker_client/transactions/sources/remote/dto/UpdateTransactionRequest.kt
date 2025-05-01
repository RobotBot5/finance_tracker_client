package com.robotbot.finance_tracker_client.transactions.sources.remote.dto

import java.math.BigDecimal
import java.time.OffsetDateTime

data class UpdateTransactionRequest(
    val amount: BigDecimal? = null,
    val time: OffsetDateTime? = null,
    val categoryId: Long? = null,
    val accountId: Long? = null
)
