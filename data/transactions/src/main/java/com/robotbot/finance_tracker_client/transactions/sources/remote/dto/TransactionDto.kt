package com.robotbot.finance_tracker_client.transactions.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity
import java.math.BigDecimal
import java.time.LocalDate

internal data class TransactionDto(
    @SerializedName("id") val id: Long,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("date") val date: LocalDate,
    @SerializedName("category") val category: CategoryDto,
    @SerializedName("account") val account: AccountDto
) {

    fun toEntity(): TransactionEntity =
        TransactionEntity(id, amount, date, category.toEntity(), account.toEntity())
}
