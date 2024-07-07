package org.zayass.assessment.exchange.domain

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

// Original list is unusably long without filtering
private val supportedCurrencies =
    listOf(
        "EUR",
        "USD",
        "GBP",
        "BTC",
        "PLN",
        "UAH",
        "RON",
        "CHF",
        "CAD",
        "JPY"
    )
    .map(Currency::getInstance)
    .toSet()

interface Converter {
    fun availableCurrencies(): List<Currency>
    fun convert(amount: Amount, currency: Currency): Amount?
}

interface ConverterProvider {
    suspend fun obtainConverter(): Result<Converter>
}

class ConversionService @Inject constructor(
    private val feeProvider: FeeProvider,
    private val converterProvider: ConverterProvider,
) {
    private val converter = flow {
        while (currentCoroutineContext().isActive) {
            val converter = converterProvider.obtainConverter().getOrNull()
            if (converter != null) {
                emit(converter)
            }

            delay(5.seconds)
        }
    }

    fun converter() = combine(converter, feeProvider.feePolicy()) { converter, feePolicy ->
        FeeAwareConverter(converter, feePolicy)
    }
}

data class ConversionResult(
    val result: Amount,
    val fee: Amount?
)

data class FeeAwareConverter(
    private val converter: Converter,
    private val feePolicy: FeePolicy
) {
    fun availableCurrencies() = converter.availableCurrencies().filter {
        it in supportedCurrencies
    }

    fun convertForward(fromAmount: Amount, currency: Currency): ConversionResult {
        val fee = feePolicy.calculateFee(fromAmount)
        val amountMinusFee =  fromAmount.copy(value = fromAmount.value - fee.value)
        val value = converter.convert(amountMinusFee, currency) ?: currency.zeroAmount()

        return ConversionResult(
            result = value,
            fee = fee
        )
    }

    fun convertBackward(toAmount: Amount, currency: Currency): ConversionResult? {
        val amount = converter.convert(toAmount, currency) ?: return null
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
