package com.robotbot.finance_tracker_client.transactions

//import com.robotbot.finance_tracker_client.transactions.account_choose.di.AccountChooseComponentFactoryModule
import com.robotbot.finance_tracker_client.transactions.category_choose.di.CategoryChooseComponentFactoryModule
import com.robotbot.finance_tracker_client.transactions.di.TransactionsDataModule
import com.robotbot.finance_tracker_client.transactions.main.di.TransactionsComponentFactoryModule
import com.robotbot.finance_tracker_client.transactions.manage.di.ManageTransactionsComponentFactoryModule
import com.robotbot.finance_tracker_client.transactions.root.di.RootTransactionComponentFactoryModule
import dagger.Module

@Module(includes = [
    TransactionsComponentFactoryModule::class,
    ManageTransactionsComponentFactoryModule::class,
//    AccountChooseComponentFactoryModule::class,
    CategoryChooseComponentFactoryModule::class,
    RootTransactionComponentFactoryModule::class,
    TransactionsDataModule::class
])
interface TransactionsFeatureModule
