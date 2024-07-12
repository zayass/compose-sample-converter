package org.zayass.assessment.exchange.db

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.zayass.assessment.exchange.domain.Account
import org.zayass.assessment.exchange.domain.AccountRepository
import org.zayass.assessment.exchange.domain.Amount
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DbAccountRepository @Inject constructor(
    private val database: AppDatabase,
    private val dao: AccountDao,
) : AccountRepository {

    override fun accounts(): Flow<List<Account>> {
        return dao.accounts().map {
            it.map(AccountEntry::toDomain)
        }
    }

    override fun transfersCount(): Flow<Int> {
        return dao.transferCount()
    }

    override suspend fun transfer(send: Amount, receive: Amount, fee: Amount?) {
        database.withTransaction {
            dao.insertOrIgnore(
                AccountEntry(
                    currency = receive.currency,
                    amount = BigDecimal.ZERO,
                ),
            )

            dao.increase(receive.currency, receive.value)
            dao.decrease(send.currency, send.value)

            dao.insert(
                TransferEntry(
                    send = send,
                    receive = receive,
                    fee = fee,
                ),
            )
        }
    }
}
