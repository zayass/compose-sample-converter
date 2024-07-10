package org.zayass.assessment.exchange.ui.converter

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.zayass.assessment.exchange.domain.Account
import org.zayass.assessment.exchange.domain.AccountRepository
import org.zayass.assessment.exchange.domain.Amount
import org.zayass.assessment.exchange.domain.ConversionResult
import org.zayass.assessment.exchange.domain.ConversionService
import org.zayass.assessment.exchange.domain.Currency
import org.zayass.assessment.exchange.domain.times
import java.math.BigDecimal
import java.util.Currency
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.zayass.assessment.exchange.domain.Converter as DomainConverter


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ConverterViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun testInitialState() = runTest {
        val viewModel = viewModel()

        viewModel.uiState.test {
            assertIs<UiState.Loading>(awaitItem())
            val ready = assertIs<UiState.Ready>(awaitItem())

            assertEquals("0", ready.sellValue)
            assertEquals(BigDecimal.ZERO, ready.sell.value)
            assertEquals(true, ready.submitEnabled)
            assertEquals(listOf(
                Currency("EUR")
            ), ready.availableToSell)
        }
    }

    @Test
    fun testChangeAmount() = runTest {
        val viewModel = viewModel()

        viewModel.uiState.test {
            skipItems(2)

            viewModel.dispatchAction(UiAction.ChangeSellAmount("100"))
            val state1 = assertIs<UiState.Ready>(awaitItem())
            assertEquals("100", state1.sellValue)
            assertEquals(BigDecimal(100), state1.sell.value)

            viewModel.dispatchAction(UiAction.ChangeSellAmount("200"))
            val state2 = assertIs<UiState.Ready>(awaitItem())
            assertEquals("200", state2.sellValue)
            assertEquals(BigDecimal(200), state2.sell.value)

            // invalid input discarded
            viewModel.dispatchAction(UiAction.ChangeSellAmount("invalid input"))
            yield()
            advanceUntilIdle()
            expectNoEvents()
        }
    }

    @Test
    fun testSubmitDisabled() = runTest {
        val viewModel = viewModel()

        viewModel.uiState.test {
            skipItems(2)

            viewModel.dispatchAction(UiAction.ChangeSellAmount("2000"))
            val state = assertIs<UiState.Ready>(awaitItem())
            assertEquals(false, state.submitEnabled)
        }
    }

    private fun viewModel(): ConverterViewModel {
        val repository = mock<AccountRepository> {
            on { accounts() } doReturn MutableStateFlow(
                listOf(
                    Account(
                        id = 1,
                        balance = Amount(
                            value = BigDecimal(1000),
                            currency = Currency("EUR")
                        )
                    )
                )
            )
        }

        val converter = mock<DomainConverter> {
            on { availableCurrencies() } doReturn listOf(
                Currency("EUR"),
                Currency("USD"),
                Currency("UAH"),
            )

            on { convertForward(any(), any()) } doAnswer { (amount: Amount, currency: Currency) ->
                ConversionResult(
                    sell = amount,
                    receive = Amount(
                        value = amount.value * BigDecimal(2),
                        currency = currency
                    ),
                    fee = amount * BigDecimal(1).movePointLeft(1)
                )
            }
        }

        val conversionService = mock<ConversionService> {
            on { converter() } doReturn flowOf(converter)
        }

        return ConverterViewModel(repository, conversionService)
    }
}