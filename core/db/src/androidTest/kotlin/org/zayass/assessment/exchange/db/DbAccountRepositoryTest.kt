package org.zayass.assessment.exchange.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.zayass.assessment.exchange.domain.Amount
import org.zayass.assessment.exchange.domain.Currency
import java.math.BigDecimal

@RunWith(AndroidJUnit4::class)
class DbAccountRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: AccountDao
    private lateinit var repository: DbAccountRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.accountsDao()
        repository = DbAccountRepository(db, dao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testTransfer() = runTest {
        val initialAccount = AccountEntry(
            currency = Currency("EUR"),
            amount = BigDecimal(1000)
        )

        dao.insertOrIgnore(initialAccount)

        repository.transfer(
            send = Amount(
                currency = Currency("EUR"),
                value = BigDecimal(100)
            ),
            receive = Amount(
                currency = Currency("USD"),
                value = BigDecimal(99)
            ),
            fee = null
        )

        dao.accounts().test {
            val (first, second) = awaitItem()

            assertEquals(BigDecimal(900).setScale(6), first.amount)
            assertEquals(BigDecimal(99).setScale(6), second.amount)
        }
    }
}