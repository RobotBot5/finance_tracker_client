package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import java.math.BigDecimal

internal data class AccountDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("currency") val currency: CurrencyDto,
    @SerializedName("balance") val balance: BigDecimal,
    @SerializedName("icon") val icon: IconDto
) {

    fun toEntity() = AccountEntity(
        id = id,
        name = name,
        currency = currency.toEntity(),
        balance = balance,
        icon = icon.toEntity()
    )
}
