package com.robotbot.finance_tracker_client.root.di
import com.robotbot.finance_tracker_client.analytics.di.AnalyticsFeatureModule
import com.robotbot.finance_tracker_client.authorize.di.AuthorizeFeatureModule
import com.robotbot.finance_tracker_client.bank_accounts.di.AccountsFeatureModule
import com.robotbot.finance_tracker_client.categories.di.CategoriesFeatureModule
import com.robotbot.finance_tracker_client.create_transfer.CreateTransferFeatureModule
import com.robotbot.finance_tracker_client.currency_choose.di.CurrenciesFeatureModule
import com.robotbot.finance_tracker_client.icon_choose.di.ChooseIconFeatureModule
import com.robotbot.finance_tracker_client.manage_accounts.di.ManageAccountsFeatureModule
import com.robotbot.finance_tracker_client.manage_categories.di.ManageCategoriesFeatureModule
import com.robotbot.finance_tracker_client.profile.di.ProfileFeatureModule
import com.robotbot.finance_tracker_client.remote.di.CoreRemoteModule
import com.robotbot.finance_tracker_client.transactions.TransactionsFeatureModule
import dagger.Module

@Module(includes = [
    AuthorizeFeatureModule::class,
    AccountsFeatureModule::class,
    ManageAccountsFeatureModule::class,
    CurrenciesFeatureModule::class,
    ChooseIconFeatureModule::class,
    CategoriesFeatureModule::class,
    ManageCategoriesFeatureModule::class,
    CreateTransferFeatureModule::class,
    TransactionsFeatureModule::class,
    AnalyticsFeatureModule::class,
    ProfileFeatureModule::class,
    CoreRemoteModule::class,
    ComponentFactoryModule::class
])
interface RootModule
