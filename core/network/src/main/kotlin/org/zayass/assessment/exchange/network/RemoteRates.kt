package org.zayass.assessment.exchange.network

import org.zayass.assessment.exchange.domain.Rates
import java.math.BigDecimal
import java.util.Currency

internal data class RemoteRates(private val ratesResponse: RatesResponse) : Rates {
    override fun availableCurrencies(): List<Currency> {
        return ratesResponse.rates.keys.sortedBy { it.currencyCode }
    }

    override fun rate(currency: Currency): BigDecimal? {
        return if (ratesResponse.base == currency) {
            BigDecimal.ONE
        } else {
            ratesResponse.rates[currency]
        }
    }

    override fun baseCurrency() = ratesResponse.base
}
