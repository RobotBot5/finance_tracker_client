package com.robotbot.finance_tracker_client.categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesComponent
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesStore
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun CategoriesContent(component: CategoriesComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val categoriesState = state.categoriesState) {
        CategoriesStore.State.CategoriesState.Initial -> {

        }
        is CategoriesStore.State.CategoriesState.Content -> {
            CategoriesList(
                categories = categoriesState.categories,
                onCategoryClicked = component::onCategoryClicked,
                onCreateCategoryClicked = component::onCreateCategoryClicked,
                modifier = modifier
            )
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
private fun CategoriesList(
    categories: List<CategoryEntity>,
    onCategoryClicked: (Long) -> Unit,
    onCreateCategoryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        columns = GridCells.Fixed(3)
    ) {
        items(
            items = categories,
            key = { it.id }
        ) {
            CategoryItem(category = it, onCategoryClicked = onCategoryClicked)
        }
        item {
            IconButton(
                modifier = Modifier.size(70.dp),
                onClick = onCreateCategoryClicked
            ) {
                Icon(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(8.dp),
                    imageVector = Icons.Default.Add,
                    tint = MaterialTheme.colorScheme.onTertiary,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    category: CategoryEntity,
    onCategoryClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { if (!category.isSystem) onCategoryClicked(category.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageLoader = if (LocalInspectionMode.current) {
            ImageLoader.Builder(LocalContext.current).build()
        } else {
            LocalCoilImageLoader.current
        }

        AsyncImage(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
                .padding(8.dp),
            model = BASE_URL.dropLast(1) + category.icon.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
            contentDescription = null
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview
@Composable
private fun CategoriesListPreview() {
    val categories = buildList {
        repeat(20) {
            add(
                CategoryEntity(
                    id = it.toLong(),
                    name = "Name ${it}",
                    type = CategoryType.EXPENSE,
                    isSystem = true,
                    icon = IconEntity(id = 1, name = "icon", path = "path")
                )
            )
        }
    }

    FinanceTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            CategoriesList(
                categories = categories,
                onCategoryClicked = {},
                onCreateCategoryClicked = {}
            )
        }
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    FinanceTrackerTheme {
        Surface {
            CategoryItem(
                category = CategoryEntity(
                    id = 1,
                    name = "Name",
                    type = CategoryType.EXPENSE,
                    isSystem = true,
                    icon = IconEntity(id = 1, name = "icon", path = "path")
                ),
                onCategoryClicked = {}
            )
        }
    }
}
