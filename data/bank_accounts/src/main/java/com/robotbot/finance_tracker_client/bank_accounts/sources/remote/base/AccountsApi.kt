package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.base

import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountsResponse
import retrofit2.http.GET

internal interface AccountsApi {

    @GET("accounts")
    suspend fun getAccounts(): AccountsResponse
}