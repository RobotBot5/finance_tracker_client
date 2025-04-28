package com.robotbot.finance_tracker_client.bank_accounts.sources

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountCreateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountUpdateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.TransferCreateRequest

internal interface RemoteAccountsSource {

    suspend fun getAccounts(): List<AccountEntity>

    suspend fun addAccount(accountCreateRequest: AccountCreateRequest)

    suspend fun getAccountById(id: Long): AccountEntity

    suspend fun updateAccount(id: Long, accountUpdateRequest: AccountUpdateRequest)

    suspend fun deleteAccount(id: Long)

    suspend fun transfer(transferCreateRequest: TransferCreateRequest)
}