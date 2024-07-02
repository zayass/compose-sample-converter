package org.zayass.assessment.exchange.network

import java.math.BigDecimal
import java.util.Currency
import java.util.Date

data class RatesResponse(
    val base: String,
    val date: Date,
    val rates: Map<Currency, BigDecimal>
)