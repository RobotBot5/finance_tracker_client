package com.robotbot.finance_tracker_client.root

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MainNavTab(
    val title: String,
    val icon: ImageVector
) {
    data object Accounts : MainNavTab("Accounts", Icons.Default.AccountBalance)
    data object Categories : MainNavTab("Categories", Icons.Default.Category)
    data object Transactions : MainNavTab("Transactions", Icons.Default.Payments)
    data object Analytics : MainNavTab("Analytics", Icons.Default.Analytics)
    data object Profile : MainNavTab("Profile", Icons.Default.Person)

    companion object {
        val tabs = listOf(Accounts, Categories, Transactions, Analytics, Profile)
    }
}
