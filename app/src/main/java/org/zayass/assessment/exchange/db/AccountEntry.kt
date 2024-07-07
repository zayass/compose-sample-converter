package org.zayass.assessment.exchange.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.zayass.assessment.exchange.domain.Account
import org.zayass.assessment.exchange.domain.Amount
import java.math.BigDecimal
import java.util.Currency

@Entity(
    tableName = "account",
    indices = [Index("currency", unique = true)]
)
data class AccountEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,

    @ColumnInfo
    val currency: Currency,
    @ColumnInfo
    val amount: BigDecimal,
)

fun AccountEntry.toDomain() = Account(
    id = id,
    balance = Amount(
        value = amount,
        currency = currency
    )
)