package com.robotbot.finance_tracker_client.bank_accounts.sources.remote

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.RemoteAccountsSource
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.base.AccountsApi
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountDto
import com.robotbot.finance_tracker_client.remote.util.wrapRetrofitExceptions
import javax.inject.Inject

internal class RealRemoteAccountsSource @Inject constructor(
    private val accountsApi: AccountsApi
) : RemoteAccountsSource {

    override suspend fun getAccounts(): List<AccountEntity> = wrapRetrofitExceptions {
        accountsApi.getAccounts().accounts.map(AccountDto::toEntity)
    }
}