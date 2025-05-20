package com.robotbot.finance_tracker_client.analytics.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.robotbot.finance_tracker_client.analytics.root.RootAnalyticsComponent.Child
import com.robotbot.finance_tracker_client.analytics.ui.AnalyticsContent
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootAnalyticsContent(component: RootAnalyticsComponent, modifier: Modifier = Modifier) {
    val childStack by component.stack.subscribeAsState()
    val activeInstance = childStack.active.instance
    val tabs = AnalyticsNavTab.tabs
    val activeTab: AnalyticsNavTab = remember(activeInstance) {
        when (activeInstance) {
            is Child.IncomeAnalytics -> AnalyticsNavTab.Income
            is Child.ExpenseAnalytics -> AnalyticsNavTab.Expenses
        }
    }
    val activeTabIndex = tabs.indexOf(activeTab)

    Column(
        modifier = modifier
    ) {
        SecondaryTabRow(
            selectedTabIndex = activeTabIndex
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = activeTab == tab,
                    onClick = { component.onTabClicked(tab) },
                    text = {
                        Text(
                            text = tab.title.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        }
        Children(
            stack = component.stack,
            animation = stackAnimation(fade() + scale()),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            when (val child = it.instance) {
                is Child.ExpenseAnalytics -> AnalyticsContent(
                    component = child.component,
                    modifier = Modifier.fillMaxWidth()
                )

                is Child.IncomeAnalytics -> AnalyticsContent(
                    component = child.component,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
