package org.zayass.assessment.exchange.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.util.Currency

class ConverterTest {
    @Test
    fun testConversions() {
        val converter = ConverterImpl(
            rates = MockRates(),
            feePolicy = PercentFee(BigDecimal(1).movePointLeft(2)),
        )

        val eur2usd = converter.convertForward(
            Amount(
                value = BigDecimal(100),
                currency = Currency("EUR"),
            ),
            Currency("USD"),
        )

        val uah2usd = converter.convertForward(
            Amount(
                value = BigDecimal(100),
                currency = Currency("UAH"),
            ),
            Currency("USD"),
        )

        assertEquals(eur2usd.fee?.value, BigDecimal(100).movePointLeft(2))
        assertEquals(
            eur2usd.receive,
            Amount(
                value = BigDecimal(19800).movePointLeft(2),
                currency = Currency("USD"),
            ),
        )

        assertEquals(uah2usd.fee?.value, BigDecimal(100).movePointLeft(2))
        assertEquals(
            uah2usd.receive,
            Amount(
                value = BigDecimal(660000).movePointLeft(2),
                currency = Currency("USD"),
            ),
        )
    }

    private class MockRates : Rates {
        private val rates = mapOf(
            Currency("USD") to BigDecimal(2),
            Currency("UAH") to BigDecimal(3).movePointLeft(2),
        )

        override fun availableCurrencies() = emptyList<Currency>()
        override fun baseCurrency(): Currency = Currency("EUR")
        override fun rate(currency: Currency) = rates[currency]
    }
}
