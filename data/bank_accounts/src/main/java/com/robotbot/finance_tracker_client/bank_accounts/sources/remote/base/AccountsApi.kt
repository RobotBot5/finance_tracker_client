package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.base

import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountCreateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountDto
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountUpdateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountsResponse
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.TransferCreateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

internal interface AccountsApi {

    @GET("accounts")
    suspend fun getAccounts(): AccountsResponse

    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") id: Long): AccountDto

    @POST("accounts")
    suspend fun addAccount(@Body account: AccountCreateRequest)

    @PATCH("accounts/{id}")
    suspend fun updateAccount(@Path("id") id: Long, @Body account: AccountUpdateRequest)

    @DELETE("accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: Long)

    @POST("accounts/transfer")
    suspend fun transfer(@Body transferCreateRequest: TransferCreateRequest)
}