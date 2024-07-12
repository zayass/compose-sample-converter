package org.zayass.assessment.exchange.db

import androidx.room.TypeConverter
import org.zayass.assessment.exchange.domain.Currency
import java.util.Currency

internal object CurrencyConverter {
    @TypeConverter
    fun toString(input: Currency?): String? {
        if (input == null) {
            return null
        }

        return input.currencyCode
    }

    @TypeConverter
    fun fromString(input: String?): Currency? {
        if (input == null) {
            return null
        }

        val parsed = runCatching { Currency(input) }
        return parsed.getOrNull()
    }
}
