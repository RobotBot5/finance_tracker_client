package com.robotbot.finance_tracker_client.transactions.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity
import java.math.BigDecimal
import java.time.OffsetDateTime

internal data class TransactionDto(
    @SerializedName("id") val id: Long,
    @SerializedName("amount") val amount: BigDecimal,
    @SerializedName("time") val time: OffsetDateTime,
    @SerializedName("category") val category: CategoryDto,
    @SerializedName("account") val account: AccountDto
) {

    fun toEntity(): TransactionEntity = TransactionEntity(id, amount, time, category.toEntity(), account.toEntity())
}
