package org.zayass.assessment.exchange.domain

import java.math.BigDecimal
import java.util.Currency

data class Account(
    val id: Long?,
    val balance: Amount
)

data class Amount(
    val value: BigDecimal,
    val currency: Currency
)

data class ConversionResult(
    val sell: Amount,
    val receive: Amount,
//    val rate: BigDecimal,
    val fee: Amount?,
)

operator fun Amount.times(factor: BigDecimal): Amount {
    return copy(
        value = value * factor
    )
}
