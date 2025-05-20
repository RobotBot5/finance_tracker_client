package com.robotbot.finance_tracker_client.analytics.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.analytics.presentation.AnalyticsComponent
import com.robotbot.finance_tracker_client.analytics.root.RootAnalyticsComponent.Child
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

internal class DefaultRootAnalyticsComponent @AssistedInject constructor(
    private val analyticsComponentFactory: AnalyticsComponent.Factory,
    @Assisted componentContext: ComponentContext
) : RootAnalyticsComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.ExpenseAnalytics,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(config: Config, childComponentContext: ComponentContext): Child =
        when (config) {
            Config.ExpenseAnalytics -> Child.ExpenseAnalytics(
                analyticsComponent(
                    CategoryType.EXPENSE,
                    childComponentContext
                )
            )

            Config.IncomeAnalytics -> Child.IncomeAnalytics(
                analyticsComponent(
                    CategoryType.INCOME,
                    childComponentContext
                )
            )
        }

    private fun analyticsComponent(
        analyticsType: CategoryType,
        componentContext: ComponentContext
    ): AnalyticsComponent =
        analyticsComponentFactory(
            analyticsType = analyticsType,
            componentContext = componentContext
        )

    override fun onTabClicked(tab: AnalyticsNavTab) {
        when (tab) {
            AnalyticsNavTab.Expenses -> navigation.bringToFront(Config.ExpenseAnalytics)
            AnalyticsNavTab.Income -> navigation.bringToFront(Config.IncomeAnalytics)
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object ExpenseAnalytics : Config

        @Serializable
        data object IncomeAnalytics : Config
    }

    @AssistedFactory
    interface Factory : RootAnalyticsComponent.Factory {
        override fun invoke(
            @Assisted componentContext: ComponentContext
        ): DefaultRootAnalyticsComponent
    }
}
