package org.zayass.assessment.exchange.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun loadAccounts(): Flow<List<AccountEntry>>
}