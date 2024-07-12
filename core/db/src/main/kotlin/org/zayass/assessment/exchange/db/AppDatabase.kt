package org.zayass.assessment.exchange.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        AccountEntry::class,
        TransferEntry::class,
    ],
    version = 2,
)
@TypeConverters(
    value = [
        BigDecimalConverter::class,
        CurrencyConverter::class,
    ],
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountDao
}
