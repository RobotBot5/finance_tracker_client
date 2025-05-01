package com.robotbot.finance_tracker_client.transactions.sources.remote.base

import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.CreateTransactionRequest
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.TransactionDto
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.TransactionsListResponse
import com.robotbot.finance_tracker_client.transactions.sources.remote.dto.UpdateTransactionRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

internal interface TransactionsApi {

    @GET("transactions")
    suspend fun getTransactions(): TransactionsListResponse

    @POST("transactions")
    suspend fun addTransaction(@Body createTransactionRequest: CreateTransactionRequest)

    @GET("transactions/{id}")
    suspend fun getTransactionById(@Path("id") id: Long): TransactionDto

    @PATCH("transactions/{id}")
    suspend fun updateTransaction(@Path("id") id: Long, @Body updateTransactionRequest: UpdateTransactionRequest)

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Long)
}