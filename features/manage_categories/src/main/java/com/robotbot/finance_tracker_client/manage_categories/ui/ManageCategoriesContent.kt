package com.robotbot.finance_tracker_client.manage_categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.entities.CategoryType.EXPENSE
import com.robotbot.finance_tracker_client.categories.entities.CategoryType.INCOME
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesComponent
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesStore.State
import com.robotbot.finance_tracker_client.manage_categories.presentation.OpenReason
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.common.ErrorStateWithReloadState
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun ManageCategoriesContent(component: ManageCategoriesComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        component.events.collect {
            when (it) {
                is ManageCategoriesComponent.Events.ErrorToast ->
                    snackBarHostState.showSnackbar(it.msg)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddings ->
        when (val currentState = state) {
            is State.Content -> {
                ManageCategoriesSuccess(
                    state = currentState,
                    onChangeCategoryName = component::onChangeCategoryName,
                    onChangeCategoryType = component::onChangeCategoryType,
                    onIconClicked = component::onIconClicked,
                    onClickCreateCategory = component::onClickCreateCategory,
                    onDeleteClicked = component::onDeleteClicked,
                    modifier = Modifier.padding(paddings)
                )
            }

            State.Error -> {
                ErrorStateWithReloadState(onReloadClicked = component::onReloadClicked)
            }

            State.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}

@Composable
private fun ManageCategoriesSuccess(
    state: State.Content,
    onChangeCategoryName: (String) -> Unit,
    onChangeCategoryType: (CategoryType) -> Unit,
    onIconClicked: () -> Unit,
    onClickCreateCategory: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageLoader = if (LocalInspectionMode.current) {
        ImageLoader.Builder(LocalContext.current).build()
    } else {
        LocalCoilImageLoader.current
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = state.categoryName,
            onValueChange = onChangeCategoryName,
            label = {
                Text(text = "Category name")
            }
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Category type: " + if (state.categoryType == EXPENSE) "Expense" else "Income",
            modifier = Modifier.clickable {
                onChangeCategoryType(if (state.categoryType == EXPENSE) INCOME else EXPENSE)
            }
        )
        Spacer(Modifier.height(16.dp))
        state.selectedIconEntity?.path?.let { path ->
            AsyncImage(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp)
                    .clickable { onIconClicked() },
                model = BASE_URL.dropLast(1) + state.selectedIconEntity.path,
                imageLoader = imageLoader,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                contentDescription = null
            )
        } ?: Icon(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(8.dp)
                .clickable { onIconClicked() },
            imageVector = Icons.Default.Clear,
            contentDescription = null
        )
        if (state.openReason == OpenReason.EDIT) {
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onClickCreateCategory,
                    enabled = !state.buttonLoading
                ) {
                    Text(text = "Edit Category")
                }
                Button(
                    onClick = onDeleteClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    enabled = !state.buttonLoading
                ) {
                    Text(text = "Delete Category")
                }
            }
        } else {
            Button(
                onClick = onClickCreateCategory,
                enabled = !state.buttonLoading
            ) {
                Text(text = "Create Category")
            }
        }
    }
}

@Preview("light")
@Composable
private fun ManageCategoriesSuccessPreviewLight() {
    val state = State.Content(
        categoryName = "",
        selectedIconEntity = null,
        categoryType = INCOME,
        buttonLoading = false,
        OpenReason.EDIT
    )

    FinanceTrackerTheme {
        ManageCategoriesSuccess(
            state = state,
            onChangeCategoryName = {},
            onChangeCategoryType = {},
            onIconClicked = {},
            onClickCreateCategory = {},
            onDeleteClicked = {}
        )
    }
}
