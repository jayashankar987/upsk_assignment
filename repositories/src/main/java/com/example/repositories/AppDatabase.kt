package com.example.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.repositories.holdings.network.HoldingResponse
import com.example.repositories.holdings.persistence.HoldingsDao

@Database(entities = [HoldingResponse.Data::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getHoldingsDao(): HoldingsDao

    companion object AppMigration {

    }
}