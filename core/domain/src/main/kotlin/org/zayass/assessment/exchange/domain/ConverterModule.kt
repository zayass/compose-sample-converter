package org.zayass.assessment.exchange.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ConverterModule {
    @Binds
    abstract fun bindConversionService(
        conversionServiceImpl: ConversionServiceImpl
    ): ConversionService
}