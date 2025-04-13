package com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto

import com.google.gson.annotations.SerializedName

internal data class AccountsResponse(
    @SerializedName("accounts") val accounts: List<AccountDto>
)
