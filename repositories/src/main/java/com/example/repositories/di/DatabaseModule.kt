package com.example.repositories.di

import android.content.Context
import androidx.room.Room
import com.example.repositories.AppDatabase
import com.example.repositories.holdings.persistence.HoldingsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, "assignment.db"
        ).build()
    }

    @Provides
    fun providesHoldingDao(appDatabase: AppDatabase): HoldingsDao = appDatabase.getHoldingsDao()
}