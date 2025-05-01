package com.robotbot.finance_tracker_client.root

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.robotbot.finance_tracker_client.authorize.ui.AuthorizeContent
import com.robotbot.finance_tracker_client.bank_accounts.ui.AccountsContent
import com.robotbot.finance_tracker_client.categories.ui.CategoriesContent
import com.robotbot.finance_tracker_client.create_transfer.choose_account.ui.ChooseAccountContent
import com.robotbot.finance_tracker_client.create_transfer.main.ui.CreateTransferContent
import com.robotbot.finance_tracker_client.currency_choose.ui.ChooseCurrencyContent
import com.robotbot.finance_tracker_client.icon_choose.ui.ChooseIconContent
import com.robotbot.finance_tracker_client.manage_accounts.ui.ManageAccountsContent
import com.robotbot.finance_tracker_client.manage_categories.ui.ManageCategoriesContent
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Accounts
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Authorize
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Categories
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ChooseAccount
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ChooseCategory
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ChooseIcon
import com.robotbot.finance_tracker_client.root.RootComponent.Child.CreateTransfer
import com.robotbot.finance_tracker_client.root.RootComponent.Child.CurrencyChoose
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ManageAccounts
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ManageCategories
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ManageTransactions
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Transactions
import com.robotbot.finance_tracker_client.transactions.category_choose.ui.ChooseCategoryContent
import com.robotbot.finance_tracker_client.transactions.main.ui.TransactionsContent
import com.robotbot.finance_tracker_client.transactions.manage.ui.ManageTransactionsContent
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    FinanceTrackerTheme {

        val childStack by component.stack.subscribeAsState()
        val activeInstance = childStack.active.instance
        val tabs = MainNavTab.tabs
        val activeTab: MainNavTab? = remember(activeInstance) {
            when (activeInstance) {
                is Accounts -> MainNavTab.Accounts
                is Categories -> MainNavTab.Categories
                is Transactions -> MainNavTab.Transactions
                else -> null
            }
        }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = activeTab == tab,
                            onClick = { component.onTabClicked(tab) },
                            icon = { Icon(imageVector = tab.icon, contentDescription = null) },
                            label = { Text(text = tab.title) }
                        )
                    }
                }
            }
        ) { paddings ->
            Children(
                stack = component.stack,
                modifier = modifier,
                animation = stackAnimation { child ->
                    when (child.instance) {
                        is Accounts, is ManageAccounts, is CurrencyChoose, is ChooseIcon, is Categories, is ManageCategories, is CreateTransfer, is ChooseAccount, is Transactions, is ManageTransactions, is ChooseCategory -> fade() + scale()
                        is Authorize -> slide(orientation = Orientation.Vertical, animationSpec = tween(600))
                    }
                }
            ) {
                when (val child = it.instance) {
                    is Authorize -> AuthorizeContent(component = child.component, modifier = Modifier.fillMaxWidth())
                    is Accounts -> AccountsContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is ManageAccounts -> ManageAccountsContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is CurrencyChoose -> ChooseCurrencyContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is ChooseIcon -> ChooseIconContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is Categories -> CategoriesContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is ManageCategories -> ManageCategoriesContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is ChooseAccount -> ChooseAccountContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is CreateTransfer -> CreateTransferContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is Transactions -> TransactionsContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is ChooseCategory -> ChooseCategoryContent(component = child.component, modifier = Modifier.fillMaxWidth().padding(paddings))
                    is ManageTransactions -> ManageTransactionsContent(component = child.component, Modifier.fillMaxWidth().padding(paddings))
                }
            }
        }
    }
}
