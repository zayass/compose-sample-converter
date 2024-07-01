package org.zayass.assessment.exchange.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "main_db")
            .build()
    }

    @Singleton
    @Provides
    fun provideAccountsDao(db: AppDatabase) = db.accountsDao()
}