package org.zayass.assessment.exchange.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    // adjust scale
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("update account set amount = amount * 10000")
            db.execSQL("update transfer set send_value = send_value * 10000")
            db.execSQL("update transfer set receive_value = receive_value * 10000")
            db.execSQL("update transfer set fee_value = fee_value * 10000")
        }
    }
}