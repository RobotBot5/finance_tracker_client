package com.robotbot.finance_tracker_client.transactions.sources.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate

data class CreateTransactionRequest(
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("categoryId") val categoryId: Long,
    @SerializedName("accountId") val accountId: Long,
    @SerializedName("date") val date: LocalDate? = null
)
