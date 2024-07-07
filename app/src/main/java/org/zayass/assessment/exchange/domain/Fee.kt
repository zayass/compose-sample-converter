package org.zayass.assessment.exchange.domain

import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

class FeeProvider @Inject constructor(
    private val repository: AccountRepository
) {
    fun feePolicy() = repository.transfersCount().map { count ->
        if (count >= 5) {
            PercentFee(BigDecimal(7).movePointLeft(3))
        } else {
            ZeroFee
        }
    }
}

interface FeePolicy {
    fun calculateFee(amount: Amount): Amount?
}

data class PercentFee(private val ratio: BigDecimal) : FeePolicy {
    override fun calculateFee(amount: Amount) =
        amount * ratio
}

object ZeroFee : FeePolicy {
    override fun calculateFee(amount: Amount) = null
}