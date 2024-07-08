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
    .map(Currency::getInstance)
    .toSet()

internal class ConversionServiceImpl @Inject internal constructor(
    private val feeProvider: FeeProvider,
    private val ratesProvider: RatesProvider,
) : ConversionService {
    private val rates = flow {
        while (currentCoroutineContext().isActive) {
            val rates = ratesProvider.obtainRates().getOrNull()
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
        it in supportedCurrencies
    }

    override fun convertForward(fromAmount: Amount, currency: Currency): ConversionResult {
        val fee = feePolicy.calculateFee(fromAmount)
        val amountMinusFee = fromAmount.copy(value = fromAmount.value - fee.value)
        val value = rates.convert(amountMinusFee, currency) ?: currency.zeroAmount()

        return ConversionResult(
            result = value,
            fee = fee
        )
    }

    override fun convertBackward(toAmount: Amount, currency: Currency): ConversionResult? {
        val amount = rates.convert(toAmount, currency) ?: return null
        val fee = feePolicy.calculateFee(amount)
        val amountPlusFee = amount.copy(
            value = amount.value + fee.value
        )

        return ConversionResult(
            result = amountPlusFee,
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
    val value = if (baseCurrency() == amount.currency) {
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