package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate

data class TransferCreateRequest(
    @SerializedName("amountFrom") val amountFrom: BigDecimal,
    @SerializedName("amountTo") val amountTo: BigDecimal? = null,
    @SerializedName("accountFromId") val accountFromId: Long,
    @SerializedName("accountToId") val accountToId: Long,
    @SerializedName("date") val date: LocalDate? = null
)
