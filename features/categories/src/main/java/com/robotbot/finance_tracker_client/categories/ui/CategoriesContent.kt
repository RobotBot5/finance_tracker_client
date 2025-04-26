package com.robotbot.finance_tracker_client.categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesComponent
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesStore
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader

@Composable
fun CategoriesContent(component: CategoriesComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val categoriesState = state.categoriesState) {
        CategoriesStore.State.CategoriesState.Initial -> {

        }
        is CategoriesStore.State.CategoriesState.Content -> {
            LazyColumn {
                items(
                    items = categoriesState.categories,
                    key = { it.id }
                ) {
                    CategoryItem(category = it)
                }
            }
        }
        CategoriesStore.State.CategoriesState.Error -> {
            Text(text = "Error")
        }
        CategoriesStore.State.CategoriesState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@Composable
private fun CategoryItem(category: CategoryEntity, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageLoader = LocalCoilImageLoader.current

        AsyncImage(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(8.dp),
            model = BASE_URL.dropLast(1) + category.icon.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.type.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}