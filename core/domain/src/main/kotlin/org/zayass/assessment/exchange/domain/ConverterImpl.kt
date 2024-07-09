package org.zayass.assessment.exchange.domain

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

// Original list is unusably long without filtering
private val supportedCurrencies =
    listOf(
        "BTC",
        "EUR",
        "USD",
        "GBP",
        "PLN",
        "UAH",
        "RON",
        "CHF",
        "CAD",
        "JPY"
    )
    .toSet()

internal class ConversionServiceImpl @Inject internal constructor(
    private val feeProvider: FeeProvider,
    private val ratesProvider: RatesProvider,
) : ConversionService {
    private val rates = flow {
        while (currentCoroutineContext().isActive) {
            val result = ratesProvider.obtainRates()
            val rates = result.getOrNull()
            if (rates != null) {
                emit(rates)
            }

            delay(5.seconds)
        }
    }

    override fun converter() = combine(rates, feeProvider.feePolicy()) { converter, feePolicy ->
        ConverterImpl(converter, feePolicy)
    }
}

internal data class ConverterImpl(
    private val rates: Rates,
    private val feePolicy: FeePolicy
) : Converter {
    override fun availableCurrencies() = rates.availableCurrencies().filter {
        it.currencyCode in supportedCurrencies
    }

    override fun convertForward(sell: Amount, currency: Currency): ConversionResult {
        val fee = feePolicy.calculateFee(sell)
        val sellMinusFee = sell.copy(value = sell.value - fee.value)
        val receive = rates.convert(sellMinusFee, currency) ?: currency.zeroAmount()

        return ConversionResult(
            sell = sell,
            receive = receive,
            fee = fee
        )
    }

    override fun convertBackward(receive: Amount, currency: Currency): ConversionResult {
        val amount = rates.convert(receive, currency)!!
        val fee = feePolicy.calculateFee(amount)
        val sell = amount.copy(
            value = amount.value + fee.value
        )

        return ConversionResult(
            sell = sell,
            receive = receive,
            fee = fee
        )
    }

    private val Amount?.value
        get() = this?.value ?: BigDecimal.ZERO

    private fun Currency.zeroAmount() = Amount(
        value = BigDecimal.ZERO,
        currency = this
    )
}

private fun Rates.convert(amount: Amount, currency: Currency): Amount? {
    val inRate = rate(currency) ?: return null
    val amountInBaseCurrency = amount.value * inRate

    val value = if (baseCurrency() != amount.currency) {
        val outRate = rate(amount.currency) ?: return null

        val scale = maxOf(
            amountInBaseCurrency.scale(),
            outRate.scale(),
            2
        )

        amountInBaseCurrency.divide(outRate, scale, RoundingMode.HALF_DOWN)
    } else {
        amountInBaseCurrency
    }

    return Amount(
        value = value,
        currency = currency
    )
}
