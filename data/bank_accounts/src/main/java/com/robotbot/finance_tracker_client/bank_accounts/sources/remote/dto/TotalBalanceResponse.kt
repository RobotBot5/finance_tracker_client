package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.bank_accounts.entities.TotalBalanceEntity
import java.math.BigDecimal

internal data class TotalBalanceResponse(
    @SerializedName("totalBalance") val totalBalance: BigDecimal,
    @SerializedName("targetCurrency") val targetCurrency: CurrencyDto
) {

    fun toEntity(): TotalBalanceEntity = TotalBalanceEntity(totalBalance, targetCurrency.toEntity())
}
