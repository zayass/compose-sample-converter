package org.zayass.assessment.exchange.db

import androidx.room.TypeConverter
import java.math.BigDecimal

object BigDecimalConverter {
    private const val SCALE = 6

    @TypeConverter
    fun toLong(input: BigDecimal?): Long? {
        if (input == null) {
            return null
        }

        return input.movePointRight(SCALE).toLong()
    }

    @TypeConverter
    fun fromLing(input: Long?): BigDecimal? {
        if (input == null) {
            return null
        }

        return input.toBigDecimal().movePointLeft(SCALE)
    }
}

