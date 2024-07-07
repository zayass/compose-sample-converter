package org.zayass.assessment.exchange.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.zayass.assessment.exchange.domain.Amount

@Entity(tableName = "transfer")
data class TransferEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @Embedded(prefix = "send_")
    val send: org.zayass.assessment.exchange.domain.Amount,
    @Embedded(prefix = "receive_")
    val receive: org.zayass.assessment.exchange.domain.Amount,
    @Embedded(prefix = "fee_")
    val fee: org.zayass.assessment.exchange.domain.Amount?,
)