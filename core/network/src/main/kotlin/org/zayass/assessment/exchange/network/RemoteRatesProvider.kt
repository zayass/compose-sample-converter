package org.zayass.assessment.exchange.network

import org.zayass.assessment.exchange.domain.Rates
import org.zayass.assessment.exchange.domain.RatesProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RemoteRatesProvider @Inject constructor(
    private val api: ExchangeRatesApi
) : RatesProvider {

    override suspend fun obtainRates(): Result<Rates> {
        return runCatching { RemoteRates(api.getRates()) }
    }
}
