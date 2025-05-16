package com.robotbot.finance_tracker_client.authorize.register.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.robotbot.finance_tracker_client.authorize.register.presentation.RegisterComponent

@Composable
fun RegisterContent(component: RegisterComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        component.events.collect {
            when (it) {
                is RegisterComponent.Events.ErrorToast -> snackBarHostState.showSnackbar("Unknown Error")
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
                value = state.firstName,
                onValueChange = { component.onChangeFirstName(it) },
                label = {
                    Text(text = "First Name")
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.password,
                onValueChange = { component.onChangePassword(it) },
                label = {
                    Text(text = "Password")
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.passwordConfirm,
                onValueChange = { component.onChangeConfirmPassword(it) },
                label = {
                    Text(text = "Confirm Password")
                }
            )
            Spacer(modifier = Modifier.height(64.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { component.onRegisterClicked() }
            ) {
                Text(text = "Register")
            }
        }
    }
}