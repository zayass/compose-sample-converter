package org.zayass.assessment.exchange.domain

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.Currency
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ConversionServiceTest {
    @Test
    fun testRatesRequestedEach5Seconds() = runTest {
        val feeProvider = mock<FeeProvider> {
            on { feePolicy() } doReturn MutableStateFlow(ZeroFee)
        }

        val ratesProvider = MockRatesProvider { Result.success(MockRates()) }
        val conversionService = ConversionServiceImpl(feeProvider, ratesProvider)

        conversionService.converter().test {
            advanceTimeBy(1.seconds)
            assertEquals(1, ratesProvider.count)
            advanceTimeBy(5.seconds)
            assertEquals(2, ratesProvider.count)
            advanceTimeBy(5.seconds)
            assertEquals(3, ratesProvider.count)

            cancelAndConsumeRemainingEvents()
        }

        // not updated after cancel
        advanceTimeBy(5.seconds)
        assertEquals(3, ratesProvider.count)
    }

    class MockRatesProvider(private val rates: () -> Result<Rates>) : RatesProvider {
        var count = 0
            private set

        override suspend fun obtainRates(): Result<Rates> {
            count++
            return rates()
        }
    }

    private class MockRates : Rates {
        override fun availableCurrencies() = emptyList<Currency>()
        override fun baseCurrency(): Currency = Currency("EUR")
        override fun rate(currency: Currency) = null
    }
}
