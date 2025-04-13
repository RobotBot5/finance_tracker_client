package com.robotbot.finance_tracker_client.bank_accounts.sources

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity

internal interface RemoteAccountsSource {

    suspend fun getAccounts(): List<AccountEntity>
}