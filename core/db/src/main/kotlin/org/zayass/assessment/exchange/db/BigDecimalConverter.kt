package org.zayass.assessment.exchange.db

import androidx.room.TypeConverter
import java.math.BigDecimal

object BigDecimalConverter {
    private val hundred = BigDecimal(100)

    @TypeConverter
    fun toLong(input: BigDecimal?): Long? {
        if (input == null) {
            return null
        }

        return input.multiply(hundred).toLong()
    }

    @TypeConverter
    fun fromLing(input: Long?): BigDecimal? {
        if (input == null) {
            return null
        }

        return input.toBigDecimal().divide(hundred)
    }
}

