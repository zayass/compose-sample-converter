package org.zayass.assessment.exchange.domain

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun accounts(): Flow<List<Account>>
    fun transfersCount(): Flow<Int>
    suspend fun transfer(send: Amount, receive: Amount, fee: Amount?)
}
