package com.robotbot.finance_tracker_client.transactions

import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity
import com.robotbot.finance_tracker_client.transactions.sources.RemoteTransactionsSource
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.CreateTransactionRequest
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.UpdateTransactionRequest
import javax.inject.Inject

interface TransactionsRepository {

    suspend fun getTransactions(): List<TransactionEntity>

    suspend fun getTransactionById(id: Long): TransactionEntity

    suspend fun getTransactionsByType(transactionType: CategoryType): List<TransactionEntity>

    suspend fun addTransaction(createTransactionRequest: CreateTransactionRequest)

    suspend fun deleteTransaction(id: Long)

    suspend fun updateTransaction(id: Long, updateTransactionRequest: UpdateTransactionRequest)
}

internal class RealTransactionsRepository @Inject constructor(
    private val remoteSource: RemoteTransactionsSource
) : TransactionsRepository {

    override suspend fun getTransactions(): List<TransactionEntity> {
        return remoteSource.getTransactions()
    }

    override suspend fun getTransactionById(id: Long): TransactionEntity {
        return remoteSource.getTransactionById(id)
    }

    override suspend fun getTransactionsByType(transactionType: CategoryType): List<TransactionEntity> {
        return remoteSource.getTransactionsByType(transactionType == CategoryType.EXPENSE)
    }

    override suspend fun addTransaction(createTransactionRequest: CreateTransactionRequest) {
        remoteSource.addTransaction(createTransactionRequest)
    }

    override suspend fun deleteTransaction(id: Long) {
        remoteSource.deleteTransaction(id)
    }

    override suspend fun updateTransaction(
        id: Long,
        updateTransactionRequest: UpdateTransactionRequest
    ) {
        remoteSource.updateTransaction(id, updateTransactionRequest)
    }
}
