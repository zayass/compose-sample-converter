package org.zayass.assessment.exchange.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.zayass.assessment.exchange.domain.RatesProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://developers.paysera.com/tasks/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesExchangeApi(retrofit: Retrofit) =
        retrofit.create<ExchangeRatesApi>()

    @Singleton
    @Provides
    fun providesRates(api: ExchangeRatesApi): RatesProvider =
        RemoteRatesProvider(api)
}
