package org.zayass.assessment.exchange.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.Currency

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun accounts(): Flow<List<AccountEntry>>

    @Query("SELECT count(*) FROM transfer")
    fun transferCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(account: AccountEntry): Long

    @Query("update account set amount = amount + :change where currency = :currency")
    suspend fun increase(currency: Currency, change: BigDecimal)

    @Query("update account set amount = amount - :change where currency = :currency")
    suspend fun decrease(currency: Currency, change: BigDecimal)

    @Insert
    suspend fun insert(transferEntry: TransferEntry)

}
