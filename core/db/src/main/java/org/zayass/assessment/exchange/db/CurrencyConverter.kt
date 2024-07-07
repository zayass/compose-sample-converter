package org.zayass.assessment.exchange.db

import androidx.room.TypeConverter
import java.util.Currency

object CurrencyConverter {
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

        val parsed = runCatching { Currency.getInstance(input) }
        return parsed.getOrNull()
    }
}