package com.robotbot.finance_tracker_client.manage_accounts.presentation

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.testing.TestStoreFactory
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class ManageAccountsStoreTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var store: ManageAccountsStore
    private val testStoreFactory: StoreFactory = TestStoreFactory()
    private val repository = mockk<BankAccountsRepository>()
    private val getInfoRepository = mockk<GetInfoRepository>()

    @Before
    fun setUp() {
        store = ManageAccountsStoreFactory(
            storeFactory = testStoreFactory,
            bankAccountsRepository = repository,
            getInfoRepository = getInfoRepository
        ).create(editableAccountId = null)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `change title intent updates state`() = testScope.runTest {
        val observer = store.state.test()

        store.accept(ManageAccountsStore.Intent.ChangeAccountTitle("New title"))

        observer.assertLastValue {
            accountTitle == "New title"
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `click save with invalid balance publishes error`() = testScope.runTest {
        val labelObserver = store.labels.test()
        store.accept(ManageAccountsStore.Intent.ChangeBalance("invalid"))
        store.accept(ManageAccountsStore.Intent.ClickSave)

        labelObserver.assertLastValue {
            this is ManageAccountsStore.Label.ErrorMsg && this.errorMsg == "Balance is not valid"
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `click save with missing currency publishes error`() = testScope.runTest {
        val labelObserver = store.labels.test()

        store.accept(ManageAccountsStore.Intent.ChangeBalance("100"))
        store.accept(ManageAccountsStore.Intent.ChangeAccountTitle("Title"))
        store.accept(ManageAccountsStore.Intent.ClickSave)

        labelObserver.assertLastValue {
            this is ManageAccountsStore.Label.ErrorMsg && this.errorMsg == "Currency is not valid"
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `choose currency clicked publishes choose currency label`() = testScope.runTest {
        val labelObserver = store.labels.test()
        store.accept(ManageAccountsStore.Intent.ChooseCurrencyClicked)

        labelObserver.assertLastValue {
            this is ManageAccountsStore.Label.ChooseCurrency && this.selectedCurrencyCode == null
        }
    }

    // More tests can be added for full CREATE and EDIT flows; for those, stub getInfoRepository and repository calls
}
