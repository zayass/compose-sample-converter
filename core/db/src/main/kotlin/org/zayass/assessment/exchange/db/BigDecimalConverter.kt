package org.zayass.assessment.exchange.db

import androidx.room.TypeConverter
import org.zayass.assessment.exchange.domain.loadBigDecimalRepresentation
import org.zayass.assessment.exchange.domain.toLongRepresentation
import java.math.BigDecimal

object BigDecimalConverter {
    @TypeConverter
    fun toLong(input: BigDecimal?) =
        input?.toLongRepresentation()

    @TypeConverter
    fun fromLong(input: Long?) =
        input?.loadBigDecimalRepresentation()
}

