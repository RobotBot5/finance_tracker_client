package com.robotbot.finance_tracker_client.root

import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.robotbot.finance_tracker_client.authorize.ui.AuthorizeContent
import com.robotbot.finance_tracker_client.bank_accounts.ui.AccountsContent
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Accounts
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Authorize
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    FinanceTrackerTheme {
        Children(
            stack = component.stack,
            modifier = modifier,
            animation = stackAnimation { child ->
                when (child.instance) {
                    is Accounts -> fade() + scale()
                    is Authorize -> slide(orientation = Orientation.Vertical, animationSpec = tween(600))
                }
            }
        ) {
            when (val child = it.instance) {
                is Authorize -> AuthorizeContent(component = child.component, modifier = Modifier.fillMaxWidth())
                is Accounts -> AccountsContent(component = child.component, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}