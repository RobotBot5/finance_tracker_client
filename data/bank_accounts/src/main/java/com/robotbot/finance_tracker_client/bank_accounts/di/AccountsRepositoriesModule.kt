package com.robotbot.finance_tracker_client.bank_accounts.di

import com.robotbot.finance_tracker_client.bank_accounts.BankAccountsRepository
import com.robotbot.finance_tracker_client.bank_accounts.RealBankAccountsRepository
import dagger.Binds
import dagger.Module

@Module
internal interface AccountsRepositoriesModule {

    @Binds
    fun bindBankAccountRepository(
        impl: RealBankAccountsRepository
    ): BankAccountsRepository
}