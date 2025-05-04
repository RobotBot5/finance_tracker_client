package com.robotbot.finance_tracker_client.transactions.category_choose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseComponent
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun ChooseCategoryContent(component: CategoryChooseComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        ChooseCategory(
            categories = state.categoriesList,
            selectedCategoryId = state.yetSelectedCategoryId,
            onCategoryClicked = component::onCategoryClicked,
            selectedCategoryType = state.selectedCategoryType,
            onChangeCategoryType = component::onChangeCategoryType,
            modifier = modifier
        )
    }
}

@Composable
private fun ChooseCategory(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onCategoryClicked: (Long) -> Unit,
    onChangeCategoryType: (CategoryType) -> Unit,
    selectedCategoryType: CategoryType,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        DropDownSelectMenu(
            selectedOption = selectedCategoryType,
            onOptionSelected = onChangeCategoryType,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(categories, key = { it.id }) { category ->
                CategoryRow(
                    category = category,
                    isSelected = category.id == selectedCategoryId,
                    onClick = { onCategoryClicked(category.id) }
                )
                HorizontalDivider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownSelectMenu(
    selectedOption: CategoryType,
    onOptionSelected: (CategoryType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOption.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CategoryType.entries.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.toString()) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryRow(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val imageLoader = if (LocalInspectionMode.current) {
        ImageLoader.Builder(LocalContext.current).build()
    } else {
        LocalCoilImageLoader.current
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = if (isSelected)
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                else Color.Transparent
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = BASE_URL.dropLast(1) + category.icon.path,
            contentDescription = category.name,
            imageLoader = imageLoader,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected)
                        MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
                .padding(8.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(name = "light")
@Composable
private fun ChooseCategoryPreviewLight() {
    val categories = buildList {
        repeat(10) {
            add(
                CategoryEntity(
                    id = it.toLong(),
                    name = "Category $it",
                    type = CategoryType.EXPENSE,
                    isSystem = true,
                    icon = IconEntity(id = 1, name = "", path = "")
                )
            )
        }
    }

    FinanceTrackerTheme {
        Surface {
            ChooseCategory(
                categories = categories,
                selectedCategoryId = 1,
                onCategoryClicked = {},
                selectedCategoryType = CategoryType.EXPENSE,
                onChangeCategoryType = {}
            )
        }
    }
}

@Preview(name = "dark")
@Composable
private fun ChooseCategoryPreviewDark() {
    val categories = buildList {
        repeat(10) {
            add(
                CategoryEntity(
                    id = it.toLong(),
                    name = "Category $it",
                    type = CategoryType.EXPENSE,
                    isSystem = true,
                    icon = IconEntity(id = 1, name = "", path = "")
                )
            )
        }
    }

    FinanceTrackerTheme(darkTheme = true) {
        Surface {
            ChooseCategory(
                categories = categories,
                selectedCategoryId = 1,
                onCategoryClicked = {},
                selectedCategoryType = CategoryType.EXPENSE,
                onChangeCategoryType = {}
            )
        }
    }
}
