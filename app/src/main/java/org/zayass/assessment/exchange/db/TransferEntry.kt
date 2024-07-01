package org.zayass.assessment.exchange.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "transfer")
data class TransferEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo
    val fromId: Int,
    @ColumnInfo
    val toId: Int,

    @ColumnInfo
    val fromAmount: BigDecimal,
    @ColumnInfo
    val toAmount: BigDecimal,
)