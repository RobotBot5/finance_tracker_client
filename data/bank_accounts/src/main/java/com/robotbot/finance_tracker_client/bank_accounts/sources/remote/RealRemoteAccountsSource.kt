package com.robotbot.finance_tracker_client.bank_accounts.sources.remote

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.entities.TotalBalanceEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.RemoteAccountsSource
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.base.AccountsApi
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountCreateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountDto
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountUpdateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.TransferCreateRequest
import com.robotbot.finance_tracker_client.remote.util.wrapRetrofitExceptions
import javax.inject.Inject

internal class RealRemoteAccountsSource @Inject constructor(
    private val accountsApi: AccountsApi
) : RemoteAccountsSource {

    override suspend fun getAccounts(): List<AccountEntity> = wrapRetrofitExceptions {
        accountsApi.getAccounts().accounts.map(AccountDto::toEntity)
    }

    override suspend fun addAccount(accountCreateRequest: AccountCreateRequest) = wrapRetrofitExceptions {
        accountsApi.addAccount(accountCreateRequest)
    }

    override suspend fun getAccountById(id: Long): AccountEntity = wrapRetrofitExceptions {
        accountsApi.getAccountById(id).toEntity()
    }

    override suspend fun updateAccount(id: Long, accountUpdateRequest: AccountUpdateRequest) = wrapRetrofitExceptions {
        accountsApi.updateAccount(id, accountUpdateRequest)
    }

    override suspend fun deleteAccount(id: Long) = wrapRetrofitExceptions {
        accountsApi.deleteAccount(id)
    }

    override suspend fun transfer(transferCreateRequest: TransferCreateRequest) = wrapRetrofitExceptions {
        accountsApi.transfer(transferCreateRequest)
    }

    override suspend fun getTotalBalance(): TotalBalanceEntity = wrapRetrofitExceptions {
        accountsApi.getTotalBalance().toEntity()
    }
}
