package org.zayass.assessment.exchange.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "account")
data class AccountEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo
    val currency: String,
    @ColumnInfo
    val amount: BigDecimal,
)

