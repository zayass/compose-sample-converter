package org.zayass.assessment.exchange.network

import java.math.BigDecimal
import java.util.Currency
import java.util.Date

internal data class RatesResponse(
    val base: Currency,
    val date: Date,
    val rates: Map<Currency, BigDecimal>
)