package com.robotbot.finance_tracker_client.transactions.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.categories.entities.CategoryType
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsComponent
import com.robotbot.finance_tracker_client.transactions.root.RootTransactionsComponent.Child
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

internal class DefaultRootTransactionsComponent @AssistedInject constructor(
    private val transactionsComponentFactory: TransactionsComponent.Factory,
    @Assisted("onCreateTransactionNavigate") private val onCreateTransactionNavigate: (Long?) -> Unit,
    @Assisted componentContext: ComponentContext
) : RootTransactionsComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.ExpenseTransactions,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(config: Config, childComponentContext: ComponentContext): Child =
        when (config) {
            Config.ExpenseTransactions -> Child.ExpenseTransactions(
                transactionsComponent(
                    CategoryType.EXPENSE,
                    childComponentContext
                )
            )

            Config.IncomeTransactions -> Child.IncomeTransactions(
                transactionsComponent(
                    CategoryType.INCOME,
                    childComponentContext
                )
            )
        }

    private fun transactionsComponent(
        transactionType: CategoryType,
        componentContext: ComponentContext
    ): TransactionsComponent =
        transactionsComponentFactory(
            transactionType = transactionType,
            onCreateTransactionNavigate = onCreateTransactionNavigate,
            componentContext = componentContext
        )

    override fun onTabClicked(tab: TransactionsNavTab) {
        when (tab) {
            TransactionsNavTab.Expenses -> navigation.bringToFront(Config.ExpenseTransactions)
            TransactionsNavTab.Income -> navigation.bringToFront(Config.IncomeTransactions)
        }
    }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object ExpenseTransactions : Config

        @Serializable
        data object IncomeTransactions : Config
    }

    @AssistedFactory
    interface Factory : RootTransactionsComponent.Factory {
        override fun invoke(
            @Assisted("onCreateTransactionNavigate") onCreateTransactionNavigate: (Long?) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultRootTransactionsComponent
    }
}

