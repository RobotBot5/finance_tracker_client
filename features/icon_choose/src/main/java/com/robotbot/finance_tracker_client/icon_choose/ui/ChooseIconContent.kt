package com.robotbot.finance_tracker_client.icon_choose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
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
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconComponent
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme

@Composable
fun ChooseIconContent(component: ChooseIconComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        IconsList(
            icons = state.iconsList,
            selectedIconId = state.yetSelectedIconId,
            onIconClicked = component::onIconClicked,
            modifier = modifier
        )
    }
}

@Composable
private fun IconsList(
    icons: List<IconEntity>,
    selectedIconId: Long?,
    onIconClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageLoader = if (LocalInspectionMode.current) {
        ImageLoader.Builder(LocalContext.current).build()
    } else {
        LocalCoilImageLoader.current
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = icons,
            key = { it.id },
        ) { icon ->
            Row(
                modifier = Modifier
                    .clickable { onIconClicked(icon.id) }
                    .fillParentMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(
                            if (icon.id == selectedIconId)
                                MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.secondaryContainer
                        )
                        .padding(8.dp),
                    model = BASE_URL.dropLast(1) + icon.path,
                    imageLoader = imageLoader,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = icon.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(name = "light")
@Composable
private fun IconsListPreviewLight() {
    val icons = buildList {
        repeat(10) {
            add(IconEntity(
                id = it.toLong(),
                name = "Name: $it",
                path = ""
            ))
        }
    }

    FinanceTrackerTheme {
        Surface {
            IconsList(
                icons = icons,
                selectedIconId = 2,
                onIconClicked = {}
            )
        }
    }
}

@Preview(name = "dark")
@Composable
private fun IconsListPreviewDark() {
    val icons = buildList {
        repeat(10) {
            add(IconEntity(
                id = it.toLong(),
                name = "Name: $it",
                path = ""
            ))
        }
    }

    FinanceTrackerTheme(darkTheme = true) {
        Surface {
            IconsList(
                icons = icons,
                selectedIconId = 2,
                onIconClicked = {}
            )
        }
    }
}
