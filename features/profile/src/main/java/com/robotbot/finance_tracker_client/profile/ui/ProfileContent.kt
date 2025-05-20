package com.robotbot.finance_tracker_client.profile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.profile.entities.ProfileEntity
import com.robotbot.finance_tracker_client.profile.presentation.ProfileComponent
import com.robotbot.finance_tracker_client.profile.presentation.ProfileStore
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun ProfileContent(component: ProfileComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val currentState = state) {
        is ProfileStore.State.Content -> {
            ProfileContentSuccess(
                profile = currentState.profile,
                onLogoutClicked = component::onLogoutClicked,
                onTargetCurrencyClicked = component::onTargetCurrencyClicked,
                modifier = modifier
            )
        }

        is ProfileStore.State.Error -> {
            Text(text = currentState.errorMsg)
        }

        ProfileStore.State.Loading -> {
            Text(text = "Loading")
        }
    }
}

@Composable
private fun ProfileContentSuccess(
    profile: ProfileEntity,
    onTargetCurrencyClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "First name: ${profile.name}",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Email: ${profile.email}",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.clickable { onTargetCurrencyClicked() },
            text = "Target currency: ${profile.targetCurrency.name}",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onLogoutClicked,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(170.dp)
        ) {
            Text(text = "Logout")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview("light")
@Composable
private fun ProfileContentSuccessPreviewLight() {
    val profile = ProfileEntity(
        email = "Test@mail.ru",
        name = "Tester",
        targetCurrency = CurrencyEntity(
            code = "USD",
            name = "dollars",
            symbol = "$"
        )
    )

    FinanceTrackerTheme {
        Surface {
            ProfileContentSuccess(
                profile = profile,
                onLogoutClicked = {},
                onTargetCurrencyClicked = {}
            )
        }
    }
}

@Preview(name = "dark")
@Composable
private fun ProfileContentSuccessPreviewDark() {
    val profile = ProfileEntity(
        email = "Test@mail.ru",
        name = "Tester",
        targetCurrency = CurrencyEntity(
            code = "USD",
            name = "dollars",
            symbol = "$"
        )
    )

    FinanceTrackerTheme(darkTheme = true) {
        Surface {
            ProfileContentSuccess(
                profile = profile,
                onLogoutClicked = {},
                onTargetCurrencyClicked = {}
            )
        }
    }
}
