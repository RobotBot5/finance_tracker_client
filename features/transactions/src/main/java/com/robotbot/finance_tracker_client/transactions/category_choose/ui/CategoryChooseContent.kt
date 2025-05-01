package com.robotbot.finance_tracker_client.transactions.category_choose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseComponent

@Composable
fun ChooseCategoryContent(component: CategoryChooseComponent, modifier: Modifier = Modifier) {

    val state = component.model.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        if (state.value.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(
                items = state.value.categoriesList,
                key = { it.id },
            ) {
                Text(
                    modifier = Modifier
                        .clickable { component.onCategoryClicked(it.id) },
                    text = it.name
                )
            }
        }
    }
}