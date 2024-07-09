package org.zayass.assessment.exchange.domain

import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.Currency

interface Rates {
    fun availableCurrencies(): List<Currency>
    fun baseCurrency(): Currency
    fun rate(currency: Currency): BigDecimal?
}

interface RatesProvider {
    suspend fun obtainRates(): Result<Rates>
}

interface Converter {
    fun availableCurrencies(): List<Currency>
    fun convertForward(sell: Amount, currency: Currency): ConversionResult
    fun convertBackward(receive: Amount, currency: Currency): ConversionResult
}

interface ConversionService {
    fun converter(): Flow<Converter>
}