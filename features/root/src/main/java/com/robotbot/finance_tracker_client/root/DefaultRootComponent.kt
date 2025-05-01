package com.robotbot.finance_tracker_client.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.robotbot.finance_tracker_client.authorize.presentation.AuthorizeComponent
import com.robotbot.finance_tracker_client.bank_accounts.presentation.AccountsComponent
import com.robotbot.finance_tracker_client.categories.presentation.CategoriesComponent
import com.robotbot.finance_tracker_client.create_transfer.ChangingAccountQualifier
import com.robotbot.finance_tracker_client.create_transfer.choose_account.presentation.ChooseAccountComponent
import com.robotbot.finance_tracker_client.create_transfer.main.presentation.CreateTransferComponent
import com.robotbot.finance_tracker_client.currency_choose.presentation.CurrenciesComponent
import com.robotbot.finance_tracker_client.icon_choose.presentation.ChooseIconComponent
import com.robotbot.finance_tracker_client.manage_accounts.presentation.ManageAccountsComponent
import com.robotbot.finance_tracker_client.manage_categories.presentation.ManageCategoriesComponent
import com.robotbot.finance_tracker_client.root.RootComponent.Child
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Accounts
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Categories
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ChooseAccount
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ChooseCategory
import com.robotbot.finance_tracker_client.root.RootComponent.Child.CreateTransfer
import com.robotbot.finance_tracker_client.root.RootComponent.Child.CurrencyChoose
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ManageAccounts
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ManageCategories
import com.robotbot.finance_tracker_client.root.RootComponent.Child.ManageTransactions
import com.robotbot.finance_tracker_client.root.RootComponent.Child.Transactions
import com.robotbot.finance_tracker_client.transactions.category_choose.presentation.CategoryChooseComponent
import com.robotbot.finance_tracker_client.transactions.main.presentation.TransactionsComponent
import com.robotbot.finance_tracker_client.transactions.manage.presentation.ManageTransactionsComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

internal class DefaultRootComponent @AssistedInject constructor(
    private val authorizeComponentFactory: AuthorizeComponent.Factory,
    private val accountsComponentFactory: AccountsComponent.Factory,
    private val manageAccountsComponentFactory: ManageAccountsComponent.Factory,
    private val currenciesComponentFactory: CurrenciesComponent.Factory,
    private val chooseIconComponentFactory: ChooseIconComponent.Factory,
    private val manageCategoriesComponentFactory: ManageCategoriesComponent.Factory,
    private val categoriesComponentFactory: CategoriesComponent.Factory,
    private val createTransferComponentFactory: CreateTransferComponent.Factory,
    private val chooseAccountComponentFactory: ChooseAccountComponent.Factory,
    private val transactionsComponentFactory: TransactionsComponent.Factory,
    private val manageTransactionsComponentFactory: ManageTransactionsComponent.Factory,
    private val chooseCategoryComponentFactory: CategoryChooseComponent.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Categories,
            handleBackButton = true,
            childFactory = ::child
        )

    override fun onTabClicked(tab: MainNavTab) {
        when (tab) {
            MainNavTab.Accounts -> navigation.replaceAll(Config.Accounts)
            MainNavTab.Categories -> navigation.replaceAll(Config.Categories)
            MainNavTab.Transactions -> navigation.replaceAll(Config.Transactions)
        }
    }

    private fun child(config: Config, childComponentContext: ComponentContext): Child =
        when (config) {
            is Config.Authorize -> Child.Authorize(authorizeComponent(childComponentContext))
            is Config.Accounts -> Accounts(accountsComponent(childComponentContext))
            is Config.ManageAccounts -> ManageAccounts(
                manageAccountsComponent(
                    config.editableAccountId,
                    childComponentContext
                )
            )
            is Config.CurrencyChoose -> CurrencyChoose(
                currenciesComponent(
                    config.selectedCurrencyCode,
                    childComponentContext
                )
            )
            is Config.ChooseIcon -> Child.ChooseIcon(
                chooseIconComponent(
                    config.yetSelectedIconId,
                    childComponentContext
                )
            )
            Config.Categories -> Categories(categoriesComponent(childComponentContext))
            is Config.ManageCategories -> ManageCategories(
                manageCategoriesComponent(
                    config.editableCategoryId,
                    childComponentContext
                )
            )
            is Config.ChooseAccount -> ChooseAccount(
                chooseAccountComponent(
                    config.changingAccountQualifier,
                    config.yetSelectedAccountId,
                    childComponentContext
                )
            )
            Config.CreateTransfer -> CreateTransfer(
                createTransferComponent(childComponentContext)
            )
            Config.Transactions -> Transactions(transactionsComponent(childComponentContext))
            is Config.ChooseCategory -> ChooseCategory(
                chooseCategoryComponent(config.yetSelectedCategoryId, childComponentContext)
            )
            is Config.ManageTransactions -> ManageTransactions(
                manageTransactionsComponent(config.editableTransactionId, childComponentContext)
            )
        }

    private fun authorizeComponent(componentContext: ComponentContext): AuthorizeComponent =
        authorizeComponentFactory(
            onAuthSuccess = { navigation.pop() },
            componentContext = componentContext
        )

    private fun accountsComponent(componentContext: ComponentContext): AccountsComponent =
        accountsComponentFactory(
            onAuthFailed = { navigation.pushNew(Config.Authorize) },
            onCreateAccount = { navigation.pushNew(Config.ManageAccounts(null)) },
            onEditAccount = { navigation.pushNew(Config.ManageAccounts(it)) },
            onCreateTransfer = { navigation.pushNew(Config.CreateTransfer) },
            componentContext = componentContext
        )

    private fun manageAccountsComponent(
        editableAccountId: Long?,
        componentContext: ComponentContext
    ): ManageAccountsComponent =
        manageAccountsComponentFactory(
            editableAccountEntityId = editableAccountId,
            onWorkFinished = { navigation.pop() },
            onChangeCurrency = { navigation.pushNew(Config.CurrencyChoose(it)) },
            onChangeIcon = { navigation.pushNew(Config.ChooseIcon(it)) },
            componentContext = componentContext
        )

    private fun currenciesComponent(
        selectedCurrencyCode: String,
        componentContext: ComponentContext
    ): CurrenciesComponent =
        currenciesComponentFactory(
            onCurrencySelected = { newSelectedCurrencyCode ->
                navigation.pop {
                    (stack.active.instance as? ManageAccounts)?.component?.onSelectedCurrencyChanged(
                        newSelectedCurrencyCode
                    )
                }
            },
            selectedCurrencyCode = selectedCurrencyCode,
            componentContext = componentContext
        )

    private fun chooseIconComponent(
        yetSelectedIconId: Long,
        componentContext: ComponentContext
    ): ChooseIconComponent =
        chooseIconComponentFactory(
            yetSelectedIconId = yetSelectedIconId,
            onIconSelected = { newSelectedIconId ->
                navigation.pop {
                    (stack.active.instance as? ManageAccounts)?.component?.onSelectedIconChanged(
                        newSelectedIconId
                    )
                    (stack.active.instance as? ManageCategories)?.component?.onSelectedIconChanged(
                        newSelectedIconId
                    )
                }
            },
            componentContext = componentContext
        )

    private fun categoriesComponent(
        componentContext: ComponentContext
    ): CategoriesComponent = categoriesComponentFactory(
        onEditAccount = { navigation.pushNew(Config.ManageCategories(it)) },
        onCreateAccount = { navigation.pushNew(Config.ManageCategories(null)) },
        componentContext = componentContext
    )

    private fun manageCategoriesComponent(
        editableCategoryId: Long?,
        componentContext: ComponentContext
    ): ManageCategoriesComponent =
        manageCategoriesComponentFactory(
            editableCategoryEntityId = editableCategoryId,
            onWorkFinished = { navigation.pop() },
            onChangeIcon = { navigation.pushNew(Config.ChooseIcon(it)) },
            componentContext = componentContext
        )

    private fun createTransferComponent(
        componentContext: ComponentContext
    ): CreateTransferComponent =
        createTransferComponentFactory(
            onChangeAccountId = { changingAccountQualifier, yetSelectedAccountId ->
                navigation.pushNew(
                    Config.ChooseAccount(
                        changingAccountQualifier, yetSelectedAccountId
                    )
                )
            },
            onTransferCreated = { navigation.pop() },
            componentContext = componentContext
        )

    private fun chooseAccountComponent(
        changingAccountQualifier: ChangingAccountQualifier,
        yetSelectedAccountId: Long?,
        componentContext: ComponentContext
    ): ChooseAccountComponent =
        chooseAccountComponentFactory(
            yetSelectedAccountId = yetSelectedAccountId,
            onAccountSelected = { newSelectedAccountId ->
                navigation.pop {
                    val currentChild = stack.active.instance
                    if (currentChild is CreateTransfer) {
                        when (changingAccountQualifier) {
                            ChangingAccountQualifier.FROM -> {
                                currentChild.component.onAccountFromIdChanged(newSelectedAccountId)
                            }

                            ChangingAccountQualifier.TO -> {
                                currentChild.component.onAccountToIdChanged(newSelectedAccountId)
                            }
                        }
                    }
                    if (currentChild is ManageTransactions) {
                        currentChild.component.onAccountChanged(newSelectedAccountId)
                    }
                }
            },
            componentContext = componentContext
        )

    private fun transactionsComponent(componentContext: ComponentContext): TransactionsComponent =
        transactionsComponentFactory(
            onCreateTransactionNavigate = { navigation.pushNew(Config.ManageTransactions(it)) },
            componentContext = componentContext
        )

    private fun manageTransactionsComponent(
        editableTransactionId: Long?,
        componentContext: ComponentContext
    ): ManageTransactionsComponent =
        manageTransactionsComponentFactory(
            editableTransactionId = editableTransactionId,
            onWorkFinished = { navigation.pop() },
            onChooseCategory = { navigation.pushNew(Config.ChooseCategory(it)) },
            onChooseAccount = { navigation.pushNew(Config.ChooseAccount(ChangingAccountQualifier.TO, it)) },
            componentContext = componentContext
        )

    private fun chooseCategoryComponent(
        yetSelectedCategoryId: Long?,
        componentContext: ComponentContext
    ): CategoryChooseComponent =
        chooseCategoryComponentFactory(
            yetSelectedCategoryId = yetSelectedCategoryId,
            onCategorySelected = { newCategoryId ->
                navigation.pop {(stack.active.instance as? ManageTransactions)
                    ?.component?.onCategoryChanged(newCategoryId)
            } },
            componentContext = componentContext
        )


    @Serializable
    private sealed interface Config {

        @Serializable
        data object Authorize : Config

        @Serializable
        data object Accounts : Config

        @Serializable
        data class ManageAccounts(val editableAccountId: Long?) : Config

        @Serializable
        data class CurrencyChoose(val selectedCurrencyCode: String) : Config

        @Serializable
        data class ChooseIcon(val yetSelectedIconId: Long) : Config

        @Serializable
        data object Categories : Config

        @Serializable
        data class ManageCategories(val editableCategoryId: Long?) : Config

        @Serializable
        data object CreateTransfer : Config

        @Serializable
        data class ChooseAccount(
            val changingAccountQualifier: ChangingAccountQualifier,
            val yetSelectedAccountId: Long?
        ) : Config

        @Serializable
        data object Transactions : Config

        @Serializable
        data class ManageTransactions(val editableTransactionId: Long?) : Config

        @Serializable
        data class ChooseCategory(val yetSelectedCategoryId: Long?) : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}
