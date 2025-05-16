package com.robotbot.finance_tracker_client.root

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainNavTab(
    val title: String,
    val icon: ImageVector
) {
    data object Accounts : MainNavTab("accounts", Icons.Default.Person)
    data object Categories : MainNavTab("categories", Icons.Default.ShoppingCart)
    data object Transactions : MainNavTab("transactions", Icons.Default.Menu)
    data object Analytics : MainNavTab("analytics", Icons.Default.MailOutline)

    companion object {
        val tabs = listOf(Accounts, Categories, Transactions, Analytics)
    }
}