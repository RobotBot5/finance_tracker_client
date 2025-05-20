package com.robotbot.finance_tracker_client.bank_accounts.sources.remote

import com.robotbot.finance_tracker_client.bank_accounts.entities.AccountEntity
import com.robotbot.finance_tracker_client.bank_accounts.entities.TotalBalanceEntity
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.base.AccountsApi
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountCreateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountDto
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountUpdateRequest
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.AccountsResponse
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.CurrencyDto
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.IconDto
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.TotalBalanceResponse
import com.robotbot.finance_tracker_client.bank_accounts.sources.remote.dto.TransferCreateRequest
import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

internal class RealRemoteAccountsSourceTest {

    private lateinit var api: AccountsApi
    private lateinit var wrapper: RemoteExceptionsWrapper
    private lateinit var source: RealRemoteAccountsSource

    @Before
    fun setUp() {
        api = mockk(relaxed = true)
        wrapper = mockk(relaxed = true)
        source = RealRemoteAccountsSource(api, wrapper)

        coEvery { wrapper.wrapRetrofitExceptions(any<suspend () -> Any>()) } coAnswers {
            val block = firstArg<suspend () -> Any>()
            block()
        }
    }

    @Test
    fun `getAccounts returns mapped entities`() = runTest {
        val dto1 = AccountDto(
            1L,
            "A",
            CurrencyDto("USD", "$", "dollars"),
            BigDecimal("10"),
            IconDto(5L, "icon", "path")
        )
        val dto2 = AccountDto(
            2L,
            "B",
            CurrencyDto("EUR", "€", "euros"),
            BigDecimal("20"),
            IconDto(4L, "icon", "path")
        )
        coEvery { api.getAccounts() } returns AccountsResponse(listOf(dto1, dto2))

        val result: List<AccountEntity> = source.getAccounts()

        assertEquals(2, result.size)
        assertEquals(dto1.toEntity(), result[0])
        assertEquals(dto2.toEntity(), result[1])
        coVerify(exactly = 1) { api.getAccounts() }
    }

    @Test
    fun `addAccount delegates to api`() = runTest {
        val req = AccountCreateRequest(
            "N",
            "USD",
            5L,
            BigDecimal.ZERO
        )

        source.addAccount(req)

        coVerify(exactly = 1) { api.addAccount(req) }
    }

    @Test
    fun `getAccountById returns mapped entity`() = runTest {
        val dto = AccountDto(
            10L,
            "X",
            CurrencyDto("USD", "$", "dollars"),
            BigDecimal("100"),
            IconDto(7L, "iconX", "path")
        )
        coEvery { api.getAccountById(10L) } returns dto

        val entity: AccountEntity = source.getAccountById(10L)

        assertEquals(dto.toEntity(), entity)
        coVerify { api.getAccountById(10L) }
    }

    @Test
    fun `updateAccount delegates to api`() = runTest {
        val update = AccountUpdateRequest("U", "JPY", 9L, BigDecimal("5"))

        source.updateAccount(3L, update)

        coVerify { api.updateAccount(3L, update) }
    }

    @Test
    fun `deleteAccount delegates to api`() = runTest {
        source.deleteAccount(4L)

        coVerify { api.deleteAccount(4L) }
    }

    @Test
    fun `transfer delegates to api`() = runTest {
        val transfer = TransferCreateRequest(BigDecimal("15"), BigDecimal("10"), 1L, 2L)

        source.transfer(transfer)

        coVerify { api.transfer(transfer) }
    }

    @Test
    fun `getTotalBalance returns mapped entity`() = runTest {
        val totalDto = TotalBalanceResponse(BigDecimal("999"), CurrencyDto("USD", "$", "dollars"))
        coEvery { api.getTotalBalance() } returns totalDto

        val total: TotalBalanceEntity = source.getTotalBalance()

        assertEquals(totalDto.toEntity(), total)
        coVerify { api.getTotalBalance() }
    }
}

