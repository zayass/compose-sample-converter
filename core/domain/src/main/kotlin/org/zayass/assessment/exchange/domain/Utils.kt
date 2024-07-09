package org.zayass.assessment.exchange.domain

import java.math.BigDecimal
import java.math.RoundingMode

private const val MAX_SCALE = 6

fun BigDecimal.round(): BigDecimal {
    return if (scale() <= MAX_SCALE) {
        this
    } else {
        setScale(MAX_SCALE, RoundingMode.DOWN)
    }
}

fun BigDecimal.toLongRepresentation() =
    round().movePointRight(MAX_SCALE).toLong()

fun Long.loadBigDecimalRepresentation(): BigDecimal =
    toBigDecimal().movePointLeft(MAX_SCALE)