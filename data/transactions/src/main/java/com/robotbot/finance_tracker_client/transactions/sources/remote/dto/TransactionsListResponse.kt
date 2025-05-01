package com.robotbot.finance_tracker_client.transactions.sources.remote.dto

import com.google.gson.annotations.SerializedName
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity

internal data class TransactionsListResponse(
    @SerializedName("transactions") val transactions: List<TransactionDto>
) {

    fun toEntities(): List<TransactionEntity> = transactions.map(TransactionDto::toEntity)
}
