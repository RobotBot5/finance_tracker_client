package com.robotbot.finance_tracker_client.manage_categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.categories.entities.CategoryType.EXPENSE
import com.robotbot.finance_tracker_client.categories.entities.CategoryType.INCOME
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesComponent
import com.robotbot.finance_tracker_client.manage_categories.presentation.OpenReason
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader

@Composable
fun ManageCategoriesContent(component: ManageCategoriesComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    val imageLoader = LocalCoilImageLoader.current

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = state.categoryName,
            onValueChange = component::onChangeCategoryName,
            label = {
                Text(text = "Category name")
            }
        )
        Text(
            text = if (state.categoryType == EXPENSE) "Expense" else "Income",
            modifier = Modifier.clickable {
                component.onChangeCategoryType(if (state.categoryType == EXPENSE) INCOME else EXPENSE)
            }
        )
        AsyncImage(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(8.dp)
                .clickable { component.onIconClicked() },
            model = BASE_URL.dropLast(1) + state.selectedIconEntity.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
            contentDescription = null
        )
        Button(
            onClick = { component.onClickCreateCategory() },
        ) {
            Text(text = "Create Category")
        }
        if (state.openReason == OpenReason.EDIT) {
            Button(
                onClick = { component.onDeleteClicked() }
            ) {
                Text(text = "Delete")
            }
        }
    }
}