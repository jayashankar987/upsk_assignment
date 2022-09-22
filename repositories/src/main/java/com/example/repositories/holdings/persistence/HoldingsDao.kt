package com.example.repositories.holdings.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.repositories.holdings.network.HoldingResponse
import com.example.repositories.holdings.network.HoldingResponse.Data

@Dao
interface HoldingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(vararg holding: Data)

    @Query("SELECT * FROM holdings")
    suspend fun getHoldings(): List<Data>

    @Query("DELETE FROM holdings")
    suspend fun deleteAll()
}