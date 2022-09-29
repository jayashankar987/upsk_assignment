package com.example.repositories.holdings.repository

import com.example.repositories.di.IoDispatcher
import com.example.repositories.holdings.network.HoldingResponse
import com.example.repositories.holdings.network.HoldingsService
import com.example.repositories.holdings.persistence.HoldingsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import javax.inject.Inject

class HoldingsRepository @Inject constructor(
    private val holdingsService: HoldingsService,
    private val holdingsDao: HoldingsDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope
) {

    val latestHolding: Flow<List<HoldingResponse.Data>> = flow {
        val response = holdingsService.getHoldings()
        if (response.isSuccessful) {
            response.body()?.let { holdings ->
                if (holdings.error != null) {
                    throw Exception("Error in fetching latest Data")
                } else {
                    emit(holdings.data)
                    scope.launch {
                        saveHoldings(holdings.data)
                    }
                }
            }
        } else {
            emit(emptyList<HoldingResponse.Data>())
        }
    }.flowOn(ioDispatcher).conflate().onEmpty { offlineHoldings }.catch { offlineHoldings }

    private val offlineHoldings: Flow<List<HoldingResponse.Data>> = flow { holdingsDao.getHoldings() }

    private suspend fun saveHoldings(holdings: List<HoldingResponse.Data>) {
        holdingsDao.deleteAll()
        return holdingsDao.insertHoldings(*holdings.toTypedArray())
    }
}