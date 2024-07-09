package org.zayass.assessment.exchange.ui

import java.math.BigDecimal
import java.text.NumberFormat

internal fun NumberFormat.applyPrecision(value: BigDecimal) {
    val precision = value.precision()
    val scale = value.scale()

    if (precision < scale && value.compareTo(BigDecimal.ZERO) != 0) {
        minimumFractionDigits = scale - precision + 3
    } else {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
}
