package com.robotbot.finance_tracker_client.analytics.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.robotbot.finance_tracker_client.analytics.entities.AnalyticByCategoriesEntity
import com.robotbot.finance_tracker_client.analytics.entities.AnalyticsByCategoriesEntity
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsComponent
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsStore
import com.robotbot.finance_tracker_client.categories.entities.CategoryEntity
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.remote.util.BASE_URL
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader
import com.robotbot.finance_tracker_client.ui.theme.FinanceTrackerTheme
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie

@Composable
fun AnalyticsContent(component: AnalyticsComponent, modifier: Modifier = Modifier) {

    val state by component.model.collectAsState()

    when (val analyticsState = state.analyticsState) {
        is AnalyticsStore.State.AnalyticsState.Content -> {
            AnalyticsContentList(
                analytics = analyticsState.analytics,
                modifier = modifier
            )
        }

        AnalyticsStore.State.AnalyticsState.Error -> {
            Text(text = "Error")
        }

        AnalyticsStore.State.AnalyticsState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@Composable
private fun AnalyticsContentList(
    analytics: AnalyticsByCategoriesEntity,
    modifier: Modifier = Modifier
) {
    val colorCategoryMap =
        buildMap {
            analytics.analytics.mapIndexed { index, analytic ->
                put(analytic.category.id, myPalette[index.coerceAtMost(myPalette.lastIndex)])
            }
        }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChartWithCenterLabel(
            initialData = analytics.analytics.map { analytic ->
                Pie(
                    label = analytic.category.name,
                    data = analytic.totalAmount.toDouble(),
                    color = colorCategoryMap[analytic.category.id] ?: Color.Green
                )
            }
        )
        LazyColumn {
            items(
                items = analytics.analytics,
                key = { it.category.id }
            ) { analytic ->
                CategoryAnalyticItem(
                    categoryAnalytic = analytic,
                    targetCurrency = analytics.targetCurrency,
                    colorCategoryMap = colorCategoryMap
                )
            }
        }
    }
}

@Preview
@Composable
private fun AnalyticsContentListPreview() {
    val categoryAnalyticList = buildList {
        repeat(10) {
            add(
                AnalyticByCategoriesEntity(
                    category = CategoryEntity(
                        it.toLong(), "category name $it", CategoryType.EXPENSE, false, IconEntity(
                            1, "icon", "path"
                        )
                    ),
                    totalAmount = 1000.toBigDecimal().multiply(((it + 1).toBigDecimal())),
                    percentage = 10
                )
            )
        }
    }
    val categoryAnalytic = AnalyticsByCategoriesEntity(
        analytics = categoryAnalyticList,
        totalAmount = 5000.toBigDecimal(),
        targetCurrency = CurrencyEntity("USD", "$", "dollars")
    )

        FinanceTrackerTheme {
            Surface {
                AnalyticsContentList(
                    analytics = categoryAnalytic,
                )
            }
        }
}

@Composable
private fun CategoryAnalyticItem(
    categoryAnalytic: AnalyticByCategoriesEntity,
    targetCurrency: CurrencyEntity,
    colorCategoryMap: Map<Long, Color>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
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
                .background(colorCategoryMap[categoryAnalytic.category.id] ?: Color.Green)
                .padding(8.dp),
            model = BASE_URL.dropLast(1) + categoryAnalytic.category.icon.path,
            imageLoader = imageLoader,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = categoryAnalytic.category.name,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${categoryAnalytic.percentage}%",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.width(32.dp))
        Text(
            text = "${categoryAnalytic.totalAmount}${targetCurrency.symbol}",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
private fun CategoryAnalyticsItemPreview() {
    val categoryAnalytic = AnalyticByCategoriesEntity(
        category = CategoryEntity(
            1, "category name", CategoryType.EXPENSE, false, IconEntity(
                1, "icon", "path"
            )
        ),
        totalAmount = 1000.toBigDecimal(),
        percentage = 40
    )

    FinanceTrackerTheme {
        Surface {
            CategoryAnalyticItem(
                categoryAnalytic = categoryAnalytic,
                targetCurrency = CurrencyEntity("USD", "$", "dollars"),
                colorCategoryMap = mutableMapOf(1L to Color.Green)
            )
        }
    }
}

val myPalette = listOf(
    Color.Red, // Red
    Color.Magenta, // Purple
    Color.Blue, // Blue
    Color.Cyan, // Teal
    Color.Green, // Brown
    Color.Gray  // Blue Grey
)

@Composable
fun PieChartWithCenterLabel(
    initialData: List<Pie>,
    modifier: Modifier = Modifier.size(200.dp)
) {
    var selectedLabel by remember { mutableStateOf<String?>(null) }
    var data by remember {
        mutableStateOf(
            initialData
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        PieChart(
            modifier = Modifier.matchParentSize(),
            data = data,
            selectedScale = 1.2f,
            scaleAnimEnterSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            scaleAnimExitSpec = tween(300),
            spaceDegreeAnimExitSpec = tween(300),
            selectedPaddingDegree = 4f,
            style = Pie.Style.Stroke(),
            onPieClick = {
                selectedLabel = it.label
                val pieIndex = data.indexOf(it)
                data =
                    data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
            }
        )
        selectedLabel?.let { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPieChartWithCenterLabel() {
    val data =
        listOf(
            Pie(label = "Android", data = 20.0, color = Color.Red),
            Pie(label = "Windows", data = 45.0, color = Color.Cyan),
            Pie(label = "Linux", data = 35.0, color = Color.Gray),
        )

    PieChartWithCenterLabel(
        initialData = data
    )
}
