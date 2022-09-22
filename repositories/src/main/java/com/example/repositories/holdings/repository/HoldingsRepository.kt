package com.example.repositories.holdings.repository

import com.example.repositories.holdings.network.HoldingResponse
import com.example.repositories.holdings.network.HoldingsService
import com.example.repositories.holdings.persistence.HoldingsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HoldingsRepository @Inject constructor(private val holdingsService: HoldingsService, private val holdingsDao: HoldingsDao) {

    suspend fun getUserHoldings(): List<HoldingResponse.Data> {
        val response = holdingsService.getHoldings()
        if (response.isSuccessful) {
            response.body()?.let { holdings ->
                if (holdings.error != null) {
                    throw Exception("Error in downloading holdings data ${holdings.error}")
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        saveHoldings(holdings.data)
                    }
                    return holdings.data
                }
            }
        }
        return getOfflineDataIfAvailable()
    }

    suspend fun getOfflineDataIfAvailable(): List<HoldingResponse.Data> = holdingsDao.getHoldings()

    suspend fun saveHoldings(holdings: List<HoldingResponse.Data>) {
        holdingsDao.deleteAll()
        return holdingsDao.insertHoldings(*holdings.toTypedArray())
    }
}