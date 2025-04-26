package com.robotbot.finance_tracker_client.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesComponent
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesComponent
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconComponent
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsComponent
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Authorize(val component: AuthorizeComponent) : Child

        data class Accounts(val component: AccountsComponent) : Child

        data class ManageAccounts(val component: ManageAccountsComponent) : Child

        data class CurrencyChoose(val component: CurrenciesComponent) : Child

        data class ChooseIcon(val component: ChooseIconComponent) : Child

        data class Categories(val component: CategoriesComponent) : Child

        data class ManageCategories(val component: ManageCategoriesComponent) : Child
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): RootComponent
    }
}