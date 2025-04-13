package com.robotbot.finance_tracker_client.bank_accounts

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.RemoteAccountsSource
import javax.inject.Inject

interface BankAccountsRepository {

    suspend fun getAccounts(): List<AccountEntity>
}

internal class RealBankAccountsRepository @Inject constructor(
    private val remoteAccountsSource: RemoteAccountsSource
): BankAccountsRepository {

    override suspend fun getAccounts(): List<AccountEntity> = remoteAccountsSource.getAccounts()
}