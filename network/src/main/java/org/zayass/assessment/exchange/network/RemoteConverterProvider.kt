package org.zayass.assessment.exchange.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConverterProvider @Inject constructor(
    private val api: ExchangeRatesApi
) : org.zayass.assessment.exchange.domain.ConverterProvider {
    override suspend fun obtainConverter(): Result<org.zayass.assessment.exchange.domain.Converter> {
        return runCatching { RemoteConverter(api.getRates()) }
    }
}
