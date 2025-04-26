package com.robotbot.finance_tracker_client.icon_choose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconComponent
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader

@Composable
fun ChooseIconContent(component: ChooseIconComponent, modifier: Modifier = Modifier) {

    val state = component.model.collectAsState()

    val imageLoader = LocalCoilImageLoader.current


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
                items = state.value.iconsList,
                key = { it.id },
            ) {
                Row(
                    modifier = Modifier
                        .clickable { component.onIconClicked(it.id) }
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(8.dp),
                        model = BASE_URL.dropLast(1) + it.path,
                        imageLoader = imageLoader,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondary),
                        contentDescription = null
                    )
                }
            }
        }
    }
}