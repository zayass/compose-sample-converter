package org.zayass.assessment.exchange.network

import retrofit2.http.GET

interface ExchangeRatesApi {
    @GET("currency-exchange-rates")
    suspend fun getRates(): RatesResponse
}

