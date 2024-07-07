package org.zayass.assessment.exchange.network

import org.zayass.assessment.exchange.domain.Amount
import org.zayass.assessment.exchange.domain.Converter
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

class RemoteConverter(private val ratesResponse: RatesResponse) : Converter {
    override fun availableCurrencies(): List<Currency> {
        return ratesResponse.rates.keys.sortedBy { it.currencyCode }
    }

    override fun convert(amount: Amount, currency: Currency): Amount? {
        val value = if (ratesResponse.base == amount.currency) {
            val rate = rate(currency) ?: return null
            amount.value * rate
        } else {
            val outRate = rate(amount.currency) ?: return null
            val inRate = rate(currency) ?: return null

            val scale = maxOf(
                amount.value.scale(),
                inRate.precision(),
                outRate.precision(),
                2
            )

            (amount.value * inRate).divide(outRate, scale, RoundingMode.HALF_DOWN)
        }

        return Amount(
            value = value,
            currency = currency
        )
    }

    private fun rate(currency: Currency): BigDecimal? {
        return if (ratesResponse.base == currency) {
            BigDecimal.ONE
        } else {
            ratesResponse.rates[currency]
        }
    }
}
