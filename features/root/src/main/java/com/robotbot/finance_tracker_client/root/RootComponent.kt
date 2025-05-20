package com.robotbot.finance_tracker_client.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.analytics.root.RootAnalyticsComponent
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeComponent
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.categories.main.RootCategoriesComponent
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountComponent
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferComponent
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesComponent
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconComponent
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsComponent
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesComponent
import com.robotbot.finance_tracker_client.profile.presentation.ProfileComponent
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseComponent
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsComponent
import com.robotbot.finance_tracker_client.transactions.root.RootTransactionsComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onTabClicked(tab: MainNavTab)

    sealed interface Child {

        data class Authorize(val component: AuthorizeComponent) : Child

        data class Register(val component: RegisterComponent) : Child

        data class Profile(val component: ProfileComponent) : Child

        data class Accounts(val component: AccountsComponent) : Child

        data class ManageAccounts(val component: ManageAccountsComponent) : Child

        data class CurrencyChoose(val component: CurrenciesComponent) : Child

        data class ChooseIcon(val component: ChooseIconComponent) : Child

        data class Categories(val component: RootCategoriesComponent) : Child

        data class ManageCategories(val component: ManageCategoriesComponent) : Child

        data class CreateTransfer(val component: CreateTransferComponent) : Child

        data class ChooseAccount(val component: ChooseAccountComponent) : Child

        data class Transactions(val component: RootTransactionsComponent) : Child

        data class ManageTransactions(val component: ManageTransactionsComponent) : Child

        data class ChooseCategory(val component: CategoryChooseComponent) : Child

        data class Analytics(val component: RootAnalyticsComponent) : Child
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): RootComponent
    }
}
