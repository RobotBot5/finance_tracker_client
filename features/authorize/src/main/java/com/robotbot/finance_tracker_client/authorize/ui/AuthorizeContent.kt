package com.robotbot.finance_tracker_client.authorize.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeComponent
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeStore

@Composable
fun AuthorizeContent(component: AuthorizeComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        component.events.collect {
            when (it) {
                is AuthorizeComponent.Events.AuthError -> snackBarHostState.showSnackbar(it.msg)
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                value = state.email,
                onValueChange = { component.onChangeEmail(it) },
                label = {
                    Text(text = "Email address")
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation(),
                value = state.password,
                onValueChange = { component.onChangePassword(it) },
                label = {
                    Text(text = "Password")
                }
            )
            Spacer(modifier = Modifier.height(64.dp))
            val loadingState by remember {
                derivedStateOf {
                    when (state.authState) {
                        AuthorizeStore.State.AuthState.Idle -> false
                        AuthorizeStore.State.AuthState.Loading -> true
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { component.onClickSignIn() },
                enabled = !loadingState
            ) {
                Text(text = "Sign in")
            }
            Text(
                modifier = Modifier.clickable { component.onRegisterClicked() },
                text = "Register"
            )
        }
    }
}

//@Preview
//@Composable
//private fun AuthorizeContentPreview() {
//    AuthorizeContent()
//}
