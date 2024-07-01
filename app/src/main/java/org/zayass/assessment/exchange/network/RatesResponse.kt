package org.zayass.assessment.exchange.network

import java.util.Date

data class RatesResponse(
    val base: String,
    val date: Date,
    val rates: Map<String, Double>
)