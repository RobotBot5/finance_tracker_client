package com.robotbot.finance_tracker_client.transactions

//import com.robotbot.finance_tracker_client.transactions.account_choose.di.AccountChooseComponentFactoryModule
import com.robotbot.finance_tracker_client.transactions.category_choose.di.CategoryChooseComponentFactoryModule
import com.robotbot.finance_tracker_client.transactions.di.TransactionsDataModule
import com.robotbot.finance_tracker_client.transactions.main.di.TransactionsComponentFactoryModule
import com.robotbot.finance_tracker_client.transactions.manage.di.ManageTransactionsComponentFactoryModule
import dagger.Module

@Module(includes = [
    TransactionsComponentFactoryModule::class,
    ManageTransactionsComponentFactoryModule::class,
//    AccountChooseComponentFactoryModule::class,
    CategoryChooseComponentFactoryModule::class,
    TransactionsDataModule::class
])
interface TransactionsFeatureModule