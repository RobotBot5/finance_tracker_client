package com.robotbot.finance_tracker_client.bank_accounts

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.RemoteAccountsSource
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountCreateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountUpdateRequest
import javax.inject.Inject

interface BankAccountsRepository {

    suspend fun getAccounts(): List<AccountEntity>

    suspend fun addAccount(accountCreateRequest: AccountCreateRequest)

    suspend fun getAccountById(id: Long): AccountEntity

    suspend fun updateAccount(id: Long, accountUpdateRequest: AccountUpdateRequest)

    suspend fun deleteAccount(id: Long)
}

internal class RealBankAccountsRepository @Inject constructor(
    private val remoteAccountsSource: RemoteAccountsSource
): BankAccountsRepository {

    override suspend fun getAccounts(): List<AccountEntity> = remoteAccountsSource.getAccounts()

    override suspend fun addAccount(accountCreateRequest: AccountCreateRequest) {
        remoteAccountsSource.addAccount(accountCreateRequest)
    }

    override suspend fun getAccountById(id: Long): AccountEntity {
        return remoteAccountsSource.getAccountById(id)
    }

    override suspend fun updateAccount(id: Long, accountUpdateRequest: AccountUpdateRequest) {
        remoteAccountsSource.updateAccount(id, accountUpdateRequest)
    }

    override suspend fun deleteAccount(id: Long) {
        remoteAccountsSource.deleteAccount(id)
    }
}