package com.robotbot.finance_tracker_client.transactions.sources

import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapper
import com.robotbot.finance_tracker_client.transactions.entities.TransactionEntity
import com.robotbot.finance_tracker_client.transactions.sources.remote.base.TransactionsApi
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.CreateTransactionRequest
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.UpdateTransactionRequest
import javax.inject.Inject

internal interface RemoteTransactionsSource {

    suspend fun getTransactions(): List<TransactionEntity>

    suspend fun getTransactionById(id: Long): TransactionEntity

    suspend fun getTransactionsByType(isExpense: Boolean): List<TransactionEntity>

    suspend fun addTransaction(createTransactionRequest: CreateTransactionRequest)

    suspend fun deleteTransaction(id: Long)

    suspend fun updateTransaction(id: Long, updateTransactionRequest: UpdateTransactionRequest)
}

internal class RealRemoteTransactionsSource @Inject constructor(
    private val api: TransactionsApi,
    private val wrapper: RemoteExceptionsWrapper
) : RemoteTransactionsSource {

    override suspend fun getTransactions(): List<TransactionEntity> =
        wrapper.wrapRetrofitExceptions {
        api.getTransactions().toEntities()
    }

    override suspend fun getTransactionById(id: Long): TransactionEntity =
        wrapper.wrapRetrofitExceptions {
        api.getTransactionById(id).toEntity()
    }

    override suspend fun getTransactionsByType(isExpense: Boolean): List<TransactionEntity> =
        wrapper.wrapRetrofitExceptions {
            api.getTransactionsByType(isExpense).toEntities()
        }

    override suspend fun addTransaction(createTransactionRequest: CreateTransactionRequest) =
        wrapper.wrapRetrofitExceptions {
        api.addTransaction(createTransactionRequest)
    }

    override suspend fun deleteTransaction(id: Long) = wrapper.wrapRetrofitExceptions {
        api.deleteTransaction(id)
    }

    override suspend fun updateTransaction(
        id: Long,
        updateTransactionRequest: UpdateTransactionRequest
    ) = wrapper.wrapRetrofitExceptions {
        api.updateTransaction(id, updateTransactionRequest)
    }
}
