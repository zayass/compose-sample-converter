package org.zayass.assessment.exchange.network

import org.zayass.assessment.exchange.domain.Converter
import org.zayass.assessment.exchange.domain.ConverterProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConverterProvider @Inject constructor(
    private val api: ExchangeRatesApi
) : ConverterProvider {
    override suspend fun obtainConverter(): Result<Converter> {
        return runCatching { RemoteConverter(api.getRates()) }
    }
}
