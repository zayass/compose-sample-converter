package org.zayass.assessment.exchange.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Currency

@Entity(tableName = "account")
data class AccountEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo
    val currency: Currency,
    @ColumnInfo
    val amount: BigDecimal,
)

